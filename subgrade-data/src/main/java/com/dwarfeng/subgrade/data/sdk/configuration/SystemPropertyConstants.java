package com.dwarfeng.subgrade.data.sdk.configuration;

/**
 * 数据模块系统属性常量。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class SystemPropertyConstants {

    /**
     * 数据模块服务异常代码偏移量。
     */
    public static final String EXCEPTION_CODE_OFFSET = "com.dwarfeng.subgrade.data.exception_code_offset";

    /**
     * 是否使用严格分页。
     */
    public static final String VALUE_USE_STRICT_PAGING = "com.dwarfeng.subgrade.data.paging.strict";

    /**
     * 是否记录分页修正警告。
     */
    public static final String VALUE_LOG_PAGING_WARNING = "com.dwarfeng.subgrade.data.paging.log_warning";

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
