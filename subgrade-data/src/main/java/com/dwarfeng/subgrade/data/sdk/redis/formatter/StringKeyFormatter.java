package com.dwarfeng.subgrade.data.sdk.redis.formatter;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

/**
 * 将指定的实体键格式化为文本的格式化器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public interface StringKeyFormatter<K extends Key> {

    /**
     * Redis 键匹配通配符。
     */
    String REDIS_KEY_WILDCARD_CHARACTER = "*";

    String format(K key);

    String generalFormat();
}
