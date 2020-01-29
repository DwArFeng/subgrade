package com.dwarfeng.subgrade.sdk.redis.formatter;

import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 将指定的实体键格式化为文本的格式化器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public interface StringKeyFormatter<K extends Key> {

    String format(K key);

    String generalFormat();
}
