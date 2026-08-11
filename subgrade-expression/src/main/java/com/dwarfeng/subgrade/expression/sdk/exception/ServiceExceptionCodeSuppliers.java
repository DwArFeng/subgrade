package com.dwarfeng.subgrade.expression.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessageKey;
import com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessages;

import java.util.function.Supplier;

/**
 * Expression 模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 2000;

    /**
     * 表达式解析失败。
     */
    public static final Supplier<ServiceException.Code> PARSE_FAILED =
            () -> new ServiceException.Code(
                    offset(10), ExpressionMessages.message(ExpressionMessageKey.SERVICE_EXCEPTION_PARSE_FAILED)
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
