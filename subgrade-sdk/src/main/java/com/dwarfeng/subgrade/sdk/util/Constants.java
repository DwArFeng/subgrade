package com.dwarfeng.subgrade.sdk.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量类。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class Constants {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Constants() {
        throw new IllegalStateException("禁止实例化");
    }
}
