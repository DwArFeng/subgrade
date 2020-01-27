package com.dwarfeng.subgrade.sdk.util;

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

    private Constants() {
        throw new IllegalStateException("禁止实例化");
    }
}
