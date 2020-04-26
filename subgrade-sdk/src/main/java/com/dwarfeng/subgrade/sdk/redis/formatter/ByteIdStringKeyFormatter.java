package com.dwarfeng.subgrade.sdk.redis.formatter;

import com.dwarfeng.subgrade.sdk.common.Constants;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;

import java.util.Objects;

/**
 * ByteIdKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ByteIdStringKeyFormatter implements StringKeyFormatter<ByteIdKey> {

    private String prefix;

    public ByteIdStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(ByteIdKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getByteId();
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
        return "ByteIdStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
