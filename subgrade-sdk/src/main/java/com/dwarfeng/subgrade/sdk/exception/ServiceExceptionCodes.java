package com.dwarfeng.subgrade.sdk.exception;

import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 服务异常代码。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ServiceExceptionCodes {

    /**
     * 未定义错误代码，代表未定义的错误。
     */
    public static final ServiceException.Code UNDEFINE = new ServiceException.Code(0, "undefine");
    /**
     * 主键获取失败
     */
    public static final ServiceException.Code KEY_FETCH_FAILED = new ServiceException.Code(100, "guid fetch failed");
    /**
     * 缓存异常。
     */
    public static final ServiceException.Code CACHE_FAILED = new ServiceException.Code(110, "cache failed");
    /**
     * 数据访问层异常。
     */
    public static final ServiceException.Code DAO_FAILED = new ServiceException.Code(120, "dao failed");
    /**
     * 参数验证失败。
     */
    public static final ServiceException.Code PARAM_VALIDATION_FAILED = new ServiceException.Code(130, "param validation failed");

    private ServiceExceptionCodes() {
        throw new IllegalStateException("禁止实例化");
    }
}
