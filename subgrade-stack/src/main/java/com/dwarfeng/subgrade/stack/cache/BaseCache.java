package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.CacheException;

/**
 * 基础缓存接口。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface BaseCache<K extends Key, E extends Entity<K>> extends Cache<E> {

    /**
     * 获取缓存中指定的键是否存在。
     *
     * @param key 指定的键是否存在。
     * @return 指定的键是否存在。
     */
    boolean exists(K key) throws CacheException;

    /**
     * 获取缓存中指定键对应的值。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     */
    E get(K key) throws CacheException;

    /**
     * 向缓存中推送指定的键与值。
     * <p>
     * 如果指定的键不存在，则创建。
     *
     * @param entity  指定的键对应的值。
     * @param timeout 超时时间（毫秒）。
     */
    void push(E entity, long timeout) throws CacheException;

    /**
     * 从缓存中删除指定的键。
     *
     * @param key 指定的键。
     */
    void delete(K key) throws CacheException;
}
