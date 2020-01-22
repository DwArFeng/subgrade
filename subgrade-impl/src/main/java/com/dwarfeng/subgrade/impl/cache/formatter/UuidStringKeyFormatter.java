package com.dwarfeng.subgrade.impl.cache.formatter;

import com.dwarfeng.subgrade.stack.bean.key.UuidKey;

import java.util.Objects;

/**
 * UuidKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class UuidStringKeyFormatter implements StringKeyFormatter<UuidKey> {

    private String prefix;

    public UuidStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(UuidKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getUuid();
    }

    @Override
    public String toString() {
        return "UuidStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
