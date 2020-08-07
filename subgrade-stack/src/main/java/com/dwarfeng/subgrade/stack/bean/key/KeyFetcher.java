package com.dwarfeng.subgrade.stack.bean.key;

import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

/**
 * 主键抓取器。
 *
 * <p>用于抓取主键</p>
 * <p>主键抓取器用于在无主键实体插入时向其分配一个主键。<br>
 * 如果希望使用数据访问层的主键生成机制，则重写 {@link KeyFetcher#fetchKey()} 方法，使其返回 <code>null</code> 即可。
 * <p>注意：实现该方法时，如果返回值不为 <code>null</code>，则必须保证每次抓取的主键都是唯一的。
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
