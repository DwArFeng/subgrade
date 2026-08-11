package com.dwarfeng.subgrade.web.sdk.configuration;

/**
 * Web 模块系统属性常量。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class SystemPropertyConstants {

    /**
     * Web 模块服务异常代码偏移量。
     */
    public static final String EXCEPTION_CODE_OFFSET = "com.dwarfeng.subgrade.web.exception_code_offset";

    /**
     * 是否使用通用表达式解析器解析权限需求值。
     */
    public static final String VALUE_PERMISSION_REQUIRED_USE_EXPRESSION =
            "com.dwarfeng.subgrade.web.permission_required.use_expression";

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
