package com.dwarfeng.subgrade.sdk;

/**
 * 系统属性常量类。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public final class SystemPropertyConstants {

    /**
     * 使用严格分页。
     */
    public static final String USE_STRICT_PAGING = "subgrade.useStrictPaging";

    /**
     * 记录分页警告。
     */
    public static final String LOG_PAGING_WARNING = "subgrade.logPagingWarning";

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
