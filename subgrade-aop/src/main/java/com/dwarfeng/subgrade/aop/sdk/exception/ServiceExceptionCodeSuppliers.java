package com.dwarfeng.subgrade.aop.sdk.exception;

import com.dwarfeng.subgrade.aop.internal.i18n.AopMessageKey;
import com.dwarfeng.subgrade.aop.internal.i18n.AopMessages;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.function.Supplier;

/**
 * AOP 模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 3000;

    /**
     * 拦截处理失败。
     */
    public static final Supplier<ServiceException.Code> INTERCEPTION_FAILED =
            () -> new ServiceException.Code(
                    offset(10), AopMessages.message(AopMessageKey.SERVICE_EXCEPTION_INTERCEPTION_FAILED)
            );

    // 为了程序的可扩展性，此处不做代码简化。
    @SuppressWarnings("SameParameterValue")
    private static int offset(int code) {
        return EXCEPTION_CODE_OFFSET + code;
    }

    public static int getExceptionCodeOffset() {
        return EXCEPTION_CODE_OFFSET;
    }

    /**
     * 设置异常代码偏移量。
     *
     * <p>
     * 该方法只允许在应用启动配置阶段调用，不应在并发服务运行期间动态修改。
     *
     * @param exceptionCodeOffset 指定的异常代码偏移量。
     */
    public static void setExceptionCodeOffset(int exceptionCodeOffset) {
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;
    }

    private ServiceExceptionCodeSuppliers() {
        throw new IllegalStateException("禁止实例化");
    }
}
