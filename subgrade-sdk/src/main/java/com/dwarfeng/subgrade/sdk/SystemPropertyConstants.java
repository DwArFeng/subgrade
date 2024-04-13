package com.dwarfeng.subgrade.sdk;

/**
 * 系统属性常量类。
 *
 * <p>
 * 该类中定义了一些系统属性的常量，主要用于在系统中获取系统属性的值。
 *
 * <p>
 * 以 <code>VALUE_</code> 开头的常量表示系统属性的值，指示一个固定的系统属性。<br>
 * 以 <code>FORMAT_</code> 开头的常量表示系统属性的格式化值，根据格式化参数的不同，可以指示不同的系统属性。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public final class SystemPropertyConstants {

    /**
     * 使用严格分页。
     */
    public static final String VALUE_USE_STRICT_PAGING = "subgrade.useStrictPaging";

    /**
     * 记录分页警告。
     */
    public static final String VALUE_LOG_PAGING_WARNING = "subgrade.logPagingWarning";

    /**
     * 详细的行为分析日志。
     */
    public static final String VALUE_DETAILED_BEHAVIOR_ANALYSE_LOG = "subgrade.detailedBehaviorAnalyseLog";

    /**
     * 启用所有友好性增强的键。
     */
    public static final String VALUE_FRIENDLY_ENABLE_ALL = "subgrade.enableFriendly";

    /**
     * 启用特定的友好性增强的键的格式化字符串。
     */
    public static final String FORMAT_FRIENDLY_ENABLE_SPECIFIC = "subgrade.enableFriendly.%s";

    /**
     * 使用严格分页。
     *
     * @deprecated 该常量已经过时，请使用 {@link #VALUE_USE_STRICT_PAGING}。
     */
    @Deprecated
    public static final String USE_STRICT_PAGING = VALUE_USE_STRICT_PAGING;

    /**
     * 记录分页警告。
     *
     * @deprecated 该常量已经过时，请使用 {@link #VALUE_LOG_PAGING_WARNING}。
     */
    @Deprecated
    public static final String LOG_PAGING_WARNING = VALUE_USE_STRICT_PAGING;

    /**
     * 详细的行为分析日志。
     *
     * @deprecated 该常量已经过时，请使用 {@link #VALUE_DETAILED_BEHAVIOR_ANALYSE_LOG}。
     */
    @Deprecated
    public static final String DETAILED_BEHAVIOR_ANALYSE_LOG = VALUE_USE_STRICT_PAGING;

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
