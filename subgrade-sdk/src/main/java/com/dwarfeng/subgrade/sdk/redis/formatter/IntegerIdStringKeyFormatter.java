package com.dwarfeng.subgrade.sdk.redis.formatter;

import com.dwarfeng.subgrade.sdk.common.Constants;
import com.dwarfeng.subgrade.stack.bean.key.IntegerIdKey;

import java.util.Objects;

/**
 * IntegerIdKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 1.1.4.a
 */
public class IntegerIdStringKeyFormatter implements StringKeyFormatter<IntegerIdKey> {

    private String prefix;

    public IntegerIdStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(IntegerIdKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getIntegerId();
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
        return "IntegerIdStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
