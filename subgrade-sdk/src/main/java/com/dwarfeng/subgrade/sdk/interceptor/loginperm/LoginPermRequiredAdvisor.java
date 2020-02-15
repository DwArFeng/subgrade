package com.dwarfeng.subgrade.sdk.interceptor.loginperm;

import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContext;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContextUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.LoginFailedException;
import com.dwarfeng.subgrade.stack.exception.PermissionDeniedException;
import com.dwarfeng.subgrade.stack.handler.LoginPermHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 登录权限需求增强。
 *
 * @author DwArFeng
 * @since 0.1.2-alpha
 */
@Component
@Aspect
public class LoginPermRequiredAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPermRequiredAdvisor.class);

    @Autowired
    private LoginPermHandler loginPermHandler;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.loginperm.LoginPermRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = beforeProcess(pjp);
        return pjp.proceed(args);
    }

    private Object[] beforeProcess(ProceedingJoinPoint pjp) {
        LOGGER.debug("开始检查登录...");
        Object[] args = pjp.getArgs();
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //获取方法所需的执行登录。
        LOGGER.debug("扫描 @LoginPermRequired 注解，获取方法执行所需的权限");
        LoginPermRequired loginPermRequired = method.getAnnotationsByType(LoginPermRequired.class)[0];
        LOGGER.debug("所需的权限");
        for (String s : loginPermRequired.value()) {
            LOGGER.debug("\t" + s);
        }
        //获取方法的参数。
        Parameter[] parameters = method.getParameters();
        //寻找User注解和PermissionMissingInfo类所在的参数所在的index。
        LOGGER.debug("扫描方法的参数，找出 @RequestLogin 注解和 ExceptionContext 类所在的位置...");
        int requestLoginPermIndex = -1;
        int exceptionContextIndex = -1;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestLoginPerm[] requestUsers = parameter.getAnnotationsByType(RequestLoginPerm.class);
            if (requestUsers.length > 0) {
                requestLoginPermIndex = i;
                break;
            }
        }
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (Objects.equals(parameter.getType(), ExceptionContext.class)) {
                exceptionContextIndex = i;
            }
        }
        LOGGER.debug("方法入口参数扫描完毕");
        LOGGER.debug("\trequestLoginPermIndex: " + requestLoginPermIndex);
        LOGGER.debug("\texceptionContextIndex: " + exceptionContextIndex);
        if (requestLoginPermIndex < 0) {
            throw new IllegalArgumentException("没有找到 @RequestLoginPerm 注解");
        }
        if (exceptionContextIndex < 0) {
            throw new IllegalArgumentException("没有找到 ExceptionContext 类的参数");
        }
        //访问登录处理器，确认登录情况。
        LOGGER.debug("访问登录处理器，确认登录与权限情况...");
        List<Exception> exceptions = new ArrayList<>();
        try {
            boolean login = loginPermHandler.isLogin((LongIdKey) args[requestLoginPermIndex]);
            if (!login) {
                exceptions.add(new LoginFailedException((LongIdKey) args[requestLoginPermIndex]));
            } else if (loginPermRequired.postpone()) {
                loginPermHandler.postpone((LongIdKey) args[requestLoginPermIndex]);
            }
            List<String> missingPermission =
                    loginPermHandler.getMissingPermission((LongIdKey) args[requestLoginPermIndex], Arrays.asList(loginPermRequired.value()));
            if (!missingPermission.isEmpty()) {
                exceptions.add(new PermissionDeniedException(missingPermission));
            }
        } catch (Exception e) {
            exceptions.add(e);
        }
        args[exceptionContextIndex] = ExceptionContextUtil.append((ExceptionContext) args[exceptionContextIndex], exceptions);
        return args;
    }
}
