package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContext;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContextUtil;
import com.dwarfeng.subgrade.stack.exception.LoginFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

/**
 * 默认的登录增强管理器。
 *
 * <p>
 * 该登录增强管理器需要在入口参数中定义一个拥有 {@link @RequestLogin} 注解的 {@link String} 类型的参数。
 * 这个参数的值将会被当作登录 ID。<br>
 * 同时，该登录增强管理器需要在入口参数中定义一个 {@link ExceptionContext} 类型的参数，用于存储登录失败的异常信息。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class DefaultLoginRequiredAopManager implements LoginRequiredAopManager {

    @Override
    public String getLoginId(ProceedingJoinPoint pjp) {
        // 获取方法，此处可将 signature 强转为 MethodSignature。
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // 获取方法的参数。
        Parameter[] parameters = method.getParameters();

        // 遍历参数，找到第一个拥有 RequestUser 注解的参数。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestLogin[] requestLogins = parameter.getAnnotationsByType(RequestLogin.class);
            if (requestLogins.length > 0) {
                return (String) pjp.getArgs()[i];
            }
        }
        throw new IllegalArgumentException(
                "未能在入口参数中找到 @RequestLogin 注解，请在入口参数中添加一个拥有 @RequestLogin 注解的 String 参数"
        );
    }

    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, String loginId) throws Throwable {
        // 获取 PJP 的参数。
        Object[] args = pjp.getArgs();

        // 获取方法，此处可将 signature 强转为 MethodSignature。
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // 获取方法的参数。
        Parameter[] parameters = method.getParameters();

        // 遍历参数，找到第一个 ExceptionContext 参数。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (ExceptionContext.class.isAssignableFrom(parameter.getType())) {
                List<Exception> exceptions = Collections.singletonList(new LoginFailedException(loginId));
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
