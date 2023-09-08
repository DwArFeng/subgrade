package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.impl.generation.ExceptionKeyGenerator;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

import java.util.List;

/**
 * 异常主键抓取器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 * @deprecated 使用 {@link ExceptionKeyGenerator} 代替。
 */
@Deprecated
public class ExceptionKeyFetcher<K extends Key> implements KeyFetcher<K> {

    @Override
    public K fetchKey() throws KeyFetchException {
        throw new KeyFetchException();
    }

    @Override
    public List<K> batchFetchKey(int size) throws KeyFetchException {
        throw new KeyFetchException();
    }

    @Override
    public String toString() {
        return "ExceptionKeyFetcher{}";
    }
}
