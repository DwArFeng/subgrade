package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 本地缓存处理器。
 *
 * <p>
 * 处理器在本地保存数据，必要时才与数据访问层通信，有助于提升程序效率。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface LocalCacheHandler<K extends Key, V> {

    /**
     * 获取指定的键对应的值是否存在。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     * @throws HandlerException 处理器异常。
     */
    boolean exists(K key) throws HandlerException;

    /**
     * 获取指定的键对应的值。
     *
     * <p>
     * 如果指定的键不存在，则返回 <code>null</code>。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     * @throws HandlerException 处理器异常。
     */
    V get(K key) throws HandlerException;

    /**
     * 移除指定的键对应的本地缓存。
     *
     * @param key 指定的键。
     * @return 该操作是否改变了本地缓存处理器中的数据。
     * @throws HandlerException 处理器异常。
     */
    boolean remove(K key) throws HandlerException;

    /**
     * 清除本地缓存。
     *
     * @throws HandlerException 处理器异常。
     */
    void clear() throws HandlerException;
}
