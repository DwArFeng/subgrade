package com.dwarfeng.subgrade.aop.sdk.configuration;

/**
 * AOP 模块系统属性常量。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class SystemPropertyConstants {

    /**
     * AOP 模块服务异常代码偏移量。
     */
    public static final String EXCEPTION_CODE_OFFSET = "com.dwarfeng.subgrade.aop.exception_code_offset";

    /**
     * 是否记录详细的行为分析日志。
     */
    public static final String VALUE_DETAILED_BEHAVIOR_ANALYSE_LOG =
            "com.dwarfeng.subgrade.aop.behavior_analyse.detailed_log";

    /**
     * 是否启用行为分析元数据缓存。
     */
    public static final String VALUE_BEHAVIOR_ANALYSE_METADATA_CACHE_ENABLED =
            "com.dwarfeng.subgrade.aop.behavior_analyse.metadata_cache_enabled";

    /**
     * 是否启用全部 Friendly 增强。
     */
    public static final String VALUE_FRIENDLY_ENABLE_ALL =
            "com.dwarfeng.subgrade.aop.friendly.enable_all";

    /**
     * 特定 Friendly 增强开关的格式字符串。
     */
    public static final String FORMAT_FRIENDLY_ENABLE_SPECIFIC =
            "com.dwarfeng.subgrade.aop.friendly.enable.%s";

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
