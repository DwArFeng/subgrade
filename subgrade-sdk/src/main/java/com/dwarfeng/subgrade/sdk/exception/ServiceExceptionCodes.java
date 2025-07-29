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
     * 未定义错误代码，代表未定义的错误。
     */
    public static final ServiceException.Code UNDEFINED = new ServiceException.Code(offset(1), "undefined");

    /**
     * 未定义错误代码，代表未定义的错误。
     *
     * @see #UNDEFINED
     * @deprecated 该常量由于拼写错误而被废弃，请使用 {@link #UNDEFINED}。
     */
    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    @Deprecated
    public static final ServiceException.Code UNDEFINE = UNDEFINED;

    /**
     * 主键获取失败
     */
    public static final ServiceException.Code KEY_FETCH_FAILED =
            new ServiceException.Code(offset(10), "key fetch failed");

    /**
     * 缓存异常。
     */
    public static final ServiceException.Code CACHE_FAILED =
            new ServiceException.Code(offset(20), "cache failed");

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

    /**
     * 未实现。
     */
    public static final ServiceException.Code NOT_IMPLEMENTED_YET =
            new ServiceException.Code(offset(110), "not implemented yet");

    /**
     * 生成失败。
     */
    public static final ServiceException.Code GENERATE_FAILED =
            new ServiceException.Code(offset(120), "generate failed");

    /**
     * 分页失败。
     */
    public static final ServiceException.Code PAGING_FAILED =
            new ServiceException.Code(offset(130), "paging failed");

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

        // 以新的 EXCEPTION_CODE_OFFSET 为基准，更新异常代码的值。
        UNDEFINED.setCode(offset(1));
        KEY_FETCH_FAILED.setCode(offset(10));
        CACHE_FAILED.setCode(offset(20));
        DAO_FAILED.setCode(offset(30));
        ENTITY_EXISTED.setCode(offset(31));
        ENTITY_NOT_EXIST.setCode(offset(32));
        PARAM_VALIDATION_FAILED.setCode(offset(40));
        IO_EXCEPTION.setCode(offset(50));
        PROCESS_FAILED.setCode(offset(60));
        HANDLER_FAILED.setCode(offset(70));
        PERMISSION_DENIED.setCode(offset(80));
        LOGIN_FAILED.setCode(offset(90));
        DATABASE_FAILED.setCode(offset(100));
        NOT_IMPLEMENTED_YET.setCode(offset(110));
        GENERATE_FAILED.setCode(offset(120));
        PAGING_FAILED.setCode(offset(130));
    }

    private ServiceExceptionCodes() {
        throw new IllegalStateException("禁止实例化");
    }
}
