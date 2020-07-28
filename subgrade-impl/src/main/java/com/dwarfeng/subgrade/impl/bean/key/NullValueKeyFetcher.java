package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;

/**
 * 空值主键抓取器。
 *
 * <p>
 * 返回 <code>null</code> 的主键抓取器。<br>
 * 使用该抓取器抓取主键后，实体的主键为 <code>null</code>，数据访问层插入该实体时会使用自身的主键生成机制。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class NullValueKeyFetcher<K extends Key> implements KeyFetcher<K> {

    @Override
    public K fetchKey() {
        return null;
    }
}
