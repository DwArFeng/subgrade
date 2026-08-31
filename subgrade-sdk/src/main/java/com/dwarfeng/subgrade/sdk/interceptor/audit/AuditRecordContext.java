package com.dwarfeng.subgrade.sdk.interceptor.audit;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 审计记录上下文。
 *
 * <p>
 * 该上下文描述一次被审计方法的完整调用结果，包含调用连接点、目标方法、入口参数、返回值以及异常信息。
 * 入口参数数组在构造时进行浅复制，以避免代理调用替换参数数组后影响审计处理。
 *
 * @author DwArFeng
 * @since 1.9.0
 */
public class AuditRecordContext {

    private final ProceedingJoinPoint proceedingJoinPoint;
    private final Method method;
    private final Object[] arguments;
    @Nullable
    private final Object result;
    @Nullable
    private final Throwable throwable;

    public AuditRecordContext(
            ProceedingJoinPoint proceedingJoinPoint, Method method, Object[] arguments,
            @Nullable Object result, @Nullable Throwable throwable
    ) {
        this.proceedingJoinPoint = proceedingJoinPoint;
        this.method = method;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
        this.result = result;
        this.throwable = throwable;
    }

    public ProceedingJoinPoint getProceedingJoinPoint() {
        return proceedingJoinPoint;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }

    @Nullable
    public Object getResult() {
        return result;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public boolean isSuccess() {
        return throwable == null;
    }

    @Override
    public String toString() {
        return "AuditRecordContext{" +
                "proceedingJoinPoint=" + proceedingJoinPoint +
                ", method=" + method +
                ", arguments=" + Arrays.toString(arguments) +
                ", result=" + result +
                ", throwable=" + throwable +
                '}';
    }
}
