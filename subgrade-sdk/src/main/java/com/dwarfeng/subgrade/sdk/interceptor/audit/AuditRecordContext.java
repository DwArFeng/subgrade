package com.dwarfeng.subgrade.sdk.interceptor.audit;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.annotation.Nullable;

/**
 * 审计记录上下文。
 *
 * <p>
 * 该上下文描述一次被审计方法的完整调用结果，包含调用连接点、返回值以及异常信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class AuditRecordContext {

    private final ProceedingJoinPoint pjp;
    @Nullable
    private final Object result;
    @Nullable
    private final Throwable throwable;

    public AuditRecordContext(
            ProceedingJoinPoint pjp, @Nullable Object result, @Nullable Throwable throwable
    ) {
        this.pjp = pjp;
        this.result = result;
        this.throwable = throwable;
    }

    public ProceedingJoinPoint getPjp() {
        return pjp;
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
                "pjp=" + pjp +
                ", result=" + result +
                ", throwable=" + throwable +
                '}';
    }
}
