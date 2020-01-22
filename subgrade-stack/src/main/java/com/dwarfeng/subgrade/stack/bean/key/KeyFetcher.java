package com.dwarfeng.subgrade.stack.bean.key;

import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

/**
 * 主键抓取器。
 * <p>用于抓取主键</p>
 * <p>注意：实现该方法时，必须保证每次抓取的主键都是唯一的。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public interface KeyFetcher<K extends Key> {

    /**
     * 抓取一个新的主键。
     *
     * @return 新的主键。
     * @throws KeyFetchException 主键抓取失败异常。
     */
    K fetchKey() throws KeyFetchException;
}
