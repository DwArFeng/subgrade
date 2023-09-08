package com.dwarfeng.subgrade.impl.generation;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;

/**
 * 空值主键生成器。
 *
 * <p>
 * 返回 <code>null</code> 的主键生成器。<br>
 * 使用该生成器抓取主键后，实体的主键为 <code>null</code>，数据访问层插入该实体时会使用自身的主键生成机制。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class NullValueKeyGenerator<K extends Key> implements KeyGenerator<K> {

    @Override
    public K generate() {
        return null;
    }

    @Override
    public String toString() {
        return "NullValueKeyGenerator{}";
    }
}
