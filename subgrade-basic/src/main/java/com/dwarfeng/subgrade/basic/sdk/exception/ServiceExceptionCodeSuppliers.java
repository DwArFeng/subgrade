package com.dwarfeng.subgrade.basic.sdk.exception;

import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessageKey;
import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessages;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.function.Supplier;

/**
 * 服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 0;

    /**
     * 未定义错误代码，代表未定义的错误。
     */
    public static final Supplier<ServiceException.Code> UNDEFINED =
            () -> new ServiceException.Code(
                    offset(1), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_UNDEFINED)
            );

    /**
     * 未定义错误代码，代表未定义的错误。
     *
     * @see #UNDEFINED
     * @deprecated 该常量由于拼写错误而被废弃，请使用 {@link #UNDEFINED}。
     */
    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    @Deprecated
    public static final Supplier<ServiceException.Code> UNDEFINE = UNDEFINED;

    /**
     * IO 异常。
     */
    public static final Supplier<ServiceException.Code> IO_EXCEPTION =
            () -> new ServiceException.Code(
                    offset(50), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_IO)
            );

    /**
     * 过程异常。
     */
    public static final Supplier<ServiceException.Code> PROCESS_FAILED =
            () -> new ServiceException.Code(
                    offset(60), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_PROCESS_FAILED)
            );

    /**
     * 处理器异常。
     */
    public static final Supplier<ServiceException.Code> HANDLER_FAILED =
            () -> new ServiceException.Code(
                    offset(70), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_HANDLER_FAILED)
            );

    /**
     * 未实现。
     */
    public static final Supplier<ServiceException.Code> NOT_IMPLEMENTED_YET =
            () -> new ServiceException.Code(
                    offset(110), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_NOT_IMPLEMENTED)
            );

    /**
     * 生成失败。
     */
    public static final Supplier<ServiceException.Code> GENERATE_FAILED =
            () -> new ServiceException.Code(
                    offset(120), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_GENERATE_FAILED)
            );

    /**
     * 分页失败。
     */
    public static final Supplier<ServiceException.Code> PAGING_FAILED =
            () -> new ServiceException.Code(
                    offset(130), BasicMessages.message(BasicMessageKey.SERVICE_EXCEPTION_PAGING_FAILED)
            );

    private static int offset(int i) {
        return EXCEPTION_CODE_OFFSET + i;
    }

    /**
     * 获取异常代号的偏移量。
     *
     * @return 异常代号的偏移量。
     */
    public static int getExceptionCodeOffset() {
        return EXCEPTION_CODE_OFFSET;
    }

    /**
     * 设置异常代号的偏移量。
     *
     * @param exceptionCodeOffset 指定的异常代号的偏移量。
     */
    public static void setExceptionCodeOffset(int exceptionCodeOffset) {
        // 设置 EXCEPTION_CODE_OFFSET 的值。
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;
    }

    private ServiceExceptionCodeSuppliers() {
        throw new IllegalStateException("禁止实例化");
    }
}
