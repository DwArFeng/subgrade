package com.dwarfeng.subgrade.impl.cache.formatter;

import com.dwarfeng.subgrade.sdk.util.Constants;
import com.dwarfeng.subgrade.stack.bean.key.DenseUuidKey;

import java.util.Objects;

/**
 * DenseUuidKey 的文本格式化转换器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DenseUuidStringKeyFormatter implements StringKeyFormatter<DenseUuidKey> {

    private String prefix;

    public DenseUuidStringKeyFormatter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String format(DenseUuidKey key) {
        Objects.requireNonNull(key);
        return prefix + key.getUuid();
    }

    @Override
    public String generalFormat() {
        return prefix + Constants.REDIS_KEY_WILDCARD_CHARACTER;
    }

    @Override
    public String toString() {
        return "DenseUuidStringKeyFormatter{" +
                "prefix='" + prefix + '\'' +
                '}';
    }
}
