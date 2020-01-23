package com.dwarfeng.subgrade.impl.cache.formatter;

import com.dwarfeng.subgrade.sdk.util.Constants;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

import java.util.Objects;

/**
 * LongIdKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class LongIdStringKeyFormatter implements StringKeyFormatter<LongIdKey> {

    private String prefix;

    public LongIdStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(LongIdKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getLongId();
    }

    @Override
    public String generalFormat() {
        return prefix + Constants.REDIS_KEY_WILDCARD_CHARACTER;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "LongIdStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
