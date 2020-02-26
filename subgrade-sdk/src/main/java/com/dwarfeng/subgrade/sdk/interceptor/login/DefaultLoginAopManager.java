package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContext;
import com.dwarfeng.subgrade.sdk.interceptor.ExceptionContextUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 默认的登录增强管理器。
 * <p>该登录增强需要在入口参数中定义一个拥有@RequestLogin的LongIdKey对象，以及一个ExceptionContext对象。 LoginIdKey对象
 * 用于提供登陆者的ID，ExceptionContext对象用于存储登录失败的信息。</p>
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
class DefaultLoginAopManager implements LoginAopManager {

    @Override
    public LongIdKey getLoginId(ProceedingJoinPoint pjp) {
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //获取方法的参数。
        Parameter[] parameters = method.getParameters();
        //寻找并返回RequestLogin注解所在对象。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestLogin[] requestLogins = parameter.getAnnotationsByType(RequestLogin.class);
            if (requestLogins.length > 0) {
                return (LongIdKey) pjp.getArgs()[i];
            }
        }
        throw new IllegalArgumentException("未能在入口参数中找到@RequestLogin注解，" +
                "请在入口参数中添加一个拥有@RequestLogin注解的LongIdKey对象");
    }

    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, LongIdKey loginId) throws Throwable {
        //获取pjp的参数。
        Object[] args = pjp.getArgs();
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //获取方法的参数。
        Parameter[] parameters = method.getParameters();
        //寻找并设置ExceptionContext。
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (Objects.equals(parameter.getType(), ExceptionContext.class)) {
                List<Exception> exceptions = Collections.singletonList(new LoginFailedException(loginId));
                args[i] = ExceptionContextUtil.append((ExceptionContext) args[i], exceptions);
                return pjp.proceed(args);
            }
        }
        throw new IllegalArgumentException(
                "未能在入口参数中找到ExceptionContext类，请在入口参数中添加ExceptionContext类对象");
    }
}
