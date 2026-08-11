package com.dwarfeng.subgrade.lifecycle.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessageKey;
import com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessages;

import java.util.function.Supplier;

/**
 * Lifecycle 模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 5000;

    /**
     * 生命周期操作失败。
     */
    public static final Supplier<ServiceException.Code> OPERATION_FAILED =
            () -> new ServiceException.Code(
                    offset(10), LifecycleMessages.message(LifecycleMessageKey.SERVICE_EXCEPTION_OPERATION_FAILED)
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
