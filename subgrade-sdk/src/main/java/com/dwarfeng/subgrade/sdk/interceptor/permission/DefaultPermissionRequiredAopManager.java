package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContext;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContextUtil;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.PermissionDeniedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

/**
 * 默认的权限增强管理器。
 *
 * <p>
 * 该权限增强管理器需要在入口参数中定义一个拥有 {@link RequestUser} 注解的 {@link StringIdKey} 类型的参数。
 * 这个参数的值将会被当作用户主键。<br>
 * 同时，该权限增强管理器需要在入口参数中定义一个 {@link ExceptionContext} 类型的参数，用于存储权限缺失的异常信息。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class DefaultPermissionRequiredAopManager implements PermissionRequiredAopManager {

    @Override
    public StringIdKey getUserKey(ProceedingJoinPoint pjp) {
        // 获取方法，此处可将 signature 强转为 MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // 获取方法的参数。
        Parameter[] parameters = method.getParameters();

        // 遍历参数，找到第一个拥有 RequestUser 注解的参数。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestUser[] requestLogins = parameter.getAnnotationsByType(RequestUser.class);
            if (requestLogins.length > 0) {
                return (StringIdKey) pjp.getArgs()[i];
            }
        }

        // 如果没有找到参数，则抛出异常。
        throw new IllegalArgumentException(
                "未能在入口参数中找到 @RequestUser 注解，请在入口参数中添加一个拥有 @RequestUser 注解的 StringIdKey 参数"
        );
    }

    @Override
    public Object onMissingPermission(ProceedingJoinPoint pjp, StringIdKey userKey, List<String> missingPermissions)
            throws Throwable {
        // 获取 PJP 的参数。
        Object[] args = pjp.getArgs();

        // 获取方法，此处可将 signature 强转为 MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // 获取方法的参数。
        Parameter[] parameters = method.getParameters();

        // 遍历参数，找到第一个 ExceptionContext 参数。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (ExceptionContext.class.isAssignableFrom(parameter.getType())) {
                List<Exception> exceptions = Collections.singletonList(
                        new PermissionDeniedException(missingPermissions)
                );
                args[i] = ExceptionContextUtil.append((ExceptionContext) args[i], exceptions);
                return pjp.proceed(args);
            }
        }

        // 如果没有找到 ExceptionContext 参数，则抛出异常。
        throw new IllegalArgumentException(
                "未能在入口参数中找到 ExceptionContext 参数，请在入口参数中添加一个 ExceptionContext 参数"
        );
    }
}
