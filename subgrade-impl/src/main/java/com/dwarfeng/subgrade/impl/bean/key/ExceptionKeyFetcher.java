package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

/**
 * 异常主键抓取器。
 * <p>该抓取器无法返回主键，每次调用都抛出异常。用于在不允许抓取主键的场景下做占位抓取器。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class ExceptionKeyFetcher<K extends Key> implements KeyFetcher<K> {

    @Override
    public K fetchKey() throws KeyFetchException {
        throw new KeyFetchException();
    }
}
