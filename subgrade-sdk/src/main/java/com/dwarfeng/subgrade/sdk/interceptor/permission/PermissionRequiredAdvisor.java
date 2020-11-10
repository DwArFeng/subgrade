package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContext;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContextUtil;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.PermissionDeniedException;
import com.dwarfeng.subgrade.stack.handler.PermissionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
        Object[] args = beforeProcess(pjp);
        return pjp.proceed(args);
    }

    private Object[] beforeProcess(ProceedingJoinPoint pjp) {
        LOGGER.debug("开始检查权限...");
        Object[] args = pjp.getArgs();
        //获取方法所需的执行权限。
        LOGGER.debug("扫描 @PermissionRequired 注解，获取方法执行所需的权限");
        PermissionRequired permissionRequired = AdvisorUtil.directMethodAnnotation(pjp, PermissionRequired.class);
        LOGGER.debug("所需的权限");
        for (String s : permissionRequired.value()) {
            LOGGER.debug("\t" + s);
        }
        //获取方法的参数。
        Parameter[] parameters = AdvisorUtil.directParameters(pjp);
        //寻找User注解和PermissionMissingInfo类所在的参数所在的index。
        LOGGER.debug("扫描方法的参数，找出 @RequestUser 注解和 ExceptionContext 类所在的位置...");
        int requestUserIndex = -1;
        int exceptionContextIndex = -1;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestUser[] requestUsers = parameter.getAnnotationsByType(RequestUser.class);
            if (requestUsers.length > 0) {
                requestUserIndex = i;
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
        LOGGER.debug("\trequestUserIndex: " + requestUserIndex);
        LOGGER.debug("\texceptionContextIndex: " + exceptionContextIndex);
        if (requestUserIndex < 0) {
            throw new IllegalArgumentException("没有找到 @RequestUser 注解");
        }
        if (exceptionContextIndex < 0) {
            throw new IllegalArgumentException("没有找到 ExceptionContext 类的参数");
        }
        //访问权限处理器，确认权限情况。
        LOGGER.debug("访问权限处理器，确认权限情况...");
        List<Exception> exceptions = new ArrayList<>();
        try {
            List<String> missingPermission =
                    permissionHandler.getMissingPermission((StringIdKey) args[requestUserIndex], Arrays.asList(permissionRequired.value()));
            if (!missingPermission.isEmpty()) {
                exceptions.add(new PermissionDeniedException(missingPermission));
            }
        } catch (Exception e) {
            exceptions.add(e);
        }
        args[exceptionContextIndex] = ExceptionContextUtil.append((ExceptionContext) args[exceptionContextIndex], exceptions);
        return args;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }
}
