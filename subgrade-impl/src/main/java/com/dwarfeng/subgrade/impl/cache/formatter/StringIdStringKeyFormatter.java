package com.dwarfeng.subgrade.impl.cache.formatter;

import com.dwarfeng.subgrade.sdk.util.Constants;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;

import java.util.Objects;

/**
 * StringIdKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class StringIdStringKeyFormatter implements StringKeyFormatter<StringIdKey> {

    private String prefix;

    public StringIdStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(StringIdKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getStringId();
    }

    @Override
    public String generalFormat() {
        return prefix + Constants.REDIS_KEY_WILDCARD_CHARACTER;
    }

    @Override
    public String toString() {
        return "StringIdStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
