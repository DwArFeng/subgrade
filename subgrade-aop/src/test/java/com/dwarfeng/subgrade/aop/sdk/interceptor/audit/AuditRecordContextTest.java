package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 审计记录上下文测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class AuditRecordContextTest {

    @Test
    public void shouldCopyArgumentArrayOnConstructionAndAccess() throws Exception {
        Object argument = new Object();
        Object[] arguments = {argument, null};
        AuditRecordContext context = newContext(arguments, null, null);
        arguments[0] = "changed";

        Object[] returnedArguments = context.arguments();
        assertSame(argument, returnedArguments[0]);
        assertNull(returnedArguments[1]);
        returnedArguments[0] = "changed again";
        assertSame(argument, context.arguments()[0]);
        assertNotSame(returnedArguments, context.arguments());
    }

    @Test
    public void shouldPreserveMethodAndJoinPoint() throws Exception {
        AuditRecordContext context = newContext(new Object[0], "result", null);

        assertNotNull(context.proceedingJoinPoint());
        assertEquals(Object.class.getMethod("toString"), context.method());
        assertEquals("result", context.result());
        assertTrue(context.isSuccess());
        assertNull(context.throwable());
    }

    @Test
    public void shouldDistinguishNullReturnFromFailure() throws Exception {
        AuditRecordContext success = newContext(new Object[0], null, null);
        Throwable failure = new IllegalStateException("business failed");
        AuditRecordContext failed = newContext(new Object[0], null, failure);

        assertTrue(success.isSuccess());
        assertNull(success.result());
        assertFalse(failed.isSuccess());
        assertSame(failure, failed.throwable());
    }

    /**
     * 构造仅用于属性计算的上下文；连接点不允许执行真实业务调用。
     *
     * @param arguments 入口参数。
     * @param result    返回值。
     * @param throwable 业务异常。
     * @return 测试上下文。
     * @throws NoSuchMethodException 方法不存在。
     */
    public static AuditRecordContext newContext(Object[] arguments, Object result, Throwable throwable)
            throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) Proxy.newProxyInstance(
                ProceedingJoinPoint.class.getClassLoader(), new Class<?>[]{ProceedingJoinPoint.class},
                (proxy, method, parameters) -> {
                    throw new AssertionError("Unexpected join point access: " + method.getName());
                }
        );
        Method method = Object.class.getMethod("toString");
        return new AuditRecordContext(joinPoint, method, arguments, result, throwable);
    }
}
