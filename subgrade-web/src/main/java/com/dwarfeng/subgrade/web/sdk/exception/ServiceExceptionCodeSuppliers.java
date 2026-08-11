package com.dwarfeng.subgrade.web.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessageKey;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessages;

import java.util.function.Supplier;

/**
 * Web 模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 4000;

    /**
     * 参数验证失败。
     */
    public static final Supplier<ServiceException.Code> PARAM_VALIDATION_FAILED =
            () -> new ServiceException.Code(
                    offset(40), WebMessages.message(WebMessageKey.SERVICE_EXCEPTION_PARAM_VALIDATION_FAILED)
            );

    /**
     * 权限被拒绝。
     */
    public static final Supplier<ServiceException.Code> PERMISSION_DENIED =
            () -> new ServiceException.Code(
                    offset(80), WebMessages.message(WebMessageKey.SERVICE_EXCEPTION_PERMISSION_DENIED)
            );

    /**
     * 登录失败。
     */
    public static final Supplier<ServiceException.Code> LOGIN_FAILED =
            () -> new ServiceException.Code(
                    offset(90), WebMessages.message(WebMessageKey.SERVICE_EXCEPTION_LOGIN_FAILED)
            );

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
