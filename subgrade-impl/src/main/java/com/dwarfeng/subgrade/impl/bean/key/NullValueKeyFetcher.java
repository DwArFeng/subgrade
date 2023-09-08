package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.impl.generation.NullValueKeyGenerator;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;

/**
 * 空值主键抓取器。
 *
 * @author DwArFeng
 * @since 1.1.3
 * @deprecated 使用 {@link NullValueKeyGenerator} 代替。
 */
@Deprecated
public class NullValueKeyFetcher<K extends Key> implements KeyFetcher<K> {

    @Override
    public K fetchKey() {
        return null;
    }

    @Override
    public String toString() {
        return "NullValueKeyFetcher{}";
    }
}
