package com.dwarfeng.subgrade.sdk.exception;

import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 服务异常代码。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ServiceExceptionCodes {

    private static int EXCEPTION_CODE_OFFSET = 0;

    /**
     * 主键获取失败
     */
    public static final ServiceException.Code KEY_FETCH_FAILED =
            new ServiceException.Code(offset(10), "guid fetch failed");

    /**
     * 缓存异常。
     */
    public static final ServiceException.Code CACHE_FAILED =
            new ServiceException.Code(offset(20), "cache failed");

    /**
     * 未定义错误代码，代表未定义的错误。
     */
    public static final ServiceException.Code UNDEFINE =
            new ServiceException.Code(offset(1), "undefine");

    /**
     * 数据访问层异常。
     */
    public static final ServiceException.Code DAO_FAILED =
            new ServiceException.Code(offset(30), "dao failed");

    /**
     * 实体已经存在。
     */
    public static final ServiceException.Code ENTITY_EXISTED =
            new ServiceException.Code(offset(31), "entity existed");

    /**
     * 实体不存在。
     */
    public static final ServiceException.Code ENTITY_NOT_EXIST =
            new ServiceException.Code(offset(32), "entity not existed");

    /**
     * 参数验证失败。
     */
    public static final ServiceException.Code PARAM_VALIDATION_FAILED =
            new ServiceException.Code(offset(40), "param validation failed");

    /**
     * IO异常。
     */
    public static final ServiceException.Code IO_EXCEPTION =
            new ServiceException.Code(offset(50), "io exception");

    /**
     * 过程异常。
     */
    public static final ServiceException.Code PROCESS_FAILED =
            new ServiceException.Code(offset(60), "process failed");

    /**
     * 处理器异常。
     */
    public static final ServiceException.Code HANDLER_FAILED =
            new ServiceException.Code(offset(70), "handler failed");

    /**
     * 权限拒绝。
     */
    public static final ServiceException.Code PERMISSION_DENIED =
            new ServiceException.Code(offset(80), "permission denied");

    /**
     * 登录失败。
     */
    public static final ServiceException.Code LOGIN_FAILED =
            new ServiceException.Code(offset(90), "login failed");

    /**
     * 数据库失败。
     */
    public static final ServiceException.Code DATABASE_FAILED =
            new ServiceException.Code(offset(100), "database failed");

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
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;
    }

    private ServiceExceptionCodes() {
        throw new IllegalStateException("禁止实例化");
    }
}
