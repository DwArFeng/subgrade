package com.dwarfeng.subgrade.sdk.common;

/**
 * 常量类。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class Constants {

    /**
     * Redis中的通配符。
     */
    public static final String REDIS_KEY_WILDCARD_CHARACTER = "*";

    /**
     * 异常代号的偏移量。
     */
    public static final int EXCEPTION_CODE_OFFSET = 0;

    private Constants() {
        throw new IllegalStateException("禁止实例化");
    }
}
