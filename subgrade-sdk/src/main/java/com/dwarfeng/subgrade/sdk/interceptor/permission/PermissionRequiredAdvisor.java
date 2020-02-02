package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.PermissionHandler;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 权限需求增强。
 *
 * @author DwArFeng
 * @since 0.1.0-alpha
 */
@Component
@Aspect
public class PermissionRequiredAdvisor {

    private final Logger LOGGER = LoggerFactory.getLogger(PermissionRequiredAdvisor.class);

    @Autowired
    private PermissionHandler permissionHandler;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.permission.PermissionRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.debug("开始检查权限...");
        Object[] args = pjp.getArgs();
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //获取方法所需的执行权限。
        LOGGER.debug("扫描 @PermissionRequired 注解，获取方法执行所需的权限");
        PermissionRequired permissionRequired = method.getAnnotationsByType(PermissionRequired.class)[0];
        LOGGER.debug("所需的权限");
        for (String s : permissionRequired.value()) {
            LOGGER.debug("\t" + s);
        }
        //获取方法的参数。
        Parameter[] parameters = method.getParameters();
        //寻找User注解和PermissionMissingInfo类所在的参数所在的index。
        LOGGER.debug("扫描方法的参数，找出 @User 注解和 CheckResult 类所在的位置...");
        int userIndex = -1;
        int checkResultIndex = -1;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestUser[] requestUsers = parameter.getAnnotationsByType(RequestUser.class);
            if (requestUsers.length > 0) {
                userIndex = i;
                break;
            }
        }
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (Objects.equals(parameter.getType(), CheckResult.class)) {
                checkResultIndex = i;
            }
        }
        LOGGER.debug("方法入口参数扫描完毕");
        LOGGER.debug("\tuserIndex: " + userIndex);
        LOGGER.debug("\tcheckResultIndex: " + checkResultIndex);
        if (userIndex < 0) {
            throw new IllegalArgumentException("没有找到 @User 注解");
        }
        if (checkResultIndex < 0) {
            throw new IllegalArgumentException("没有找到 CheckResult 类的参数");
        }
        //访问权限处理器，确认权限情况。
        LOGGER.debug("访问权限处理器，确认权限情况...");
        List<String> missingPermission = null;
        HandlerException handlerException = null;
        try {
            missingPermission = permissionHandler.getMissingPermission((String) args[userIndex], Arrays.asList(permissionRequired.value()));
        } catch (HandlerException e) {
            handlerException = e;
        }
        CheckResult checkResult = new CheckResult(handlerException, missingPermission);
        args[checkResultIndex] = checkResult;
        return pjp.proceed(args);
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }
}
