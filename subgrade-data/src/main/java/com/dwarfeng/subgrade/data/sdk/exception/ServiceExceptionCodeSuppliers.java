package com.dwarfeng.subgrade.data.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessageKey;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessages;

import java.util.function.Supplier;

/**
 * 数据模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 1000;

    /**
     * 缓存操作失败。
     */
    public static final Supplier<ServiceException.Code> CACHE_FAILED =
            () -> new ServiceException.Code(
                    offset(20), DataMessages.message(DataMessageKey.SERVICE_EXCEPTION_CACHE_FAILED)
            );

    /**
     * 数据访问操作失败。
     */
    public static final Supplier<ServiceException.Code> DAO_FAILED =
            () -> new ServiceException.Code(
                    offset(30), DataMessages.message(DataMessageKey.SERVICE_EXCEPTION_DAO_FAILED)
            );

    /**
     * 实体已经存在。
     */
    public static final Supplier<ServiceException.Code> ENTITY_EXISTED =
            () -> new ServiceException.Code(
                    offset(31), DataMessages.message(DataMessageKey.SERVICE_EXCEPTION_ENTITY_EXISTED)
            );

    /**
     * 实体不存在。
     */
    public static final Supplier<ServiceException.Code> ENTITY_NOT_EXIST =
            () -> new ServiceException.Code(
                    offset(32), DataMessages.message(DataMessageKey.SERVICE_EXCEPTION_ENTITY_NOT_EXIST)
            );

    /**
     * 数据库操作失败。
     */
    public static final Supplier<ServiceException.Code> DATABASE_FAILED =
            () -> new ServiceException.Code(
                    offset(100), DataMessages.message(DataMessageKey.SERVICE_EXCEPTION_DATABASE_FAILED)
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
