package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.CacheException;

import java.util.Collection;
import java.util.List;

/**
 * 列表型缓存。
 * <p>记录主键对应列表的缓存情形。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface ListCache<K extends Key, E extends Entity<? extends Key>> extends Cache {

    /**
     * 获取缓存中指定的键是否存在。
     *
     * @param key 指定的键是否存在。
     * @return 指定的键是否存在。
     */
    boolean exists(K key) throws CacheException;

    /**
     * 获取缓存中指定键对应的实体列表。
     *
     * @param key 指定的键。
     * @return 指定的键对应的实体列表。
     * @throws CacheException 缓存异常。
     */
    List<E> get(K key) throws CacheException;

    /**
     * 获取缓存中指定的键对应的实体列表的子列表。
     *
     * @param key        指定的键。
     * @param beginIndex 子列表的起始元素。
     * @param maxEntity  子列表的最大元素数量。
     * @return 指定的键对应的实体列表的子列表。
     * @throws CacheException 缓存异常。
     */
    List<E> subList(K key, int beginIndex, int maxEntity) throws CacheException;

    /**
     * 向指定的键对应的列表的左侧推入指定的元素集合。
     *
     * @param key      指定的键。
     * @param entities 指定的实体元素集合。
     * @throws CacheException 缓存异常。
     */
    void leftPush(K key, Collection<E> entities) throws CacheException;

    /**
     * 向指定的键对应的列表的右侧推入指定的元素集合。
     *
     * @param key      指定的键。
     * @param entities 指定的实体元素集合。
     * @throws CacheException 缓存异常。
     */
    void rightPush(K key, Collection<E> entities) throws CacheException;

    /**
     * 从缓存中删除指定的键。
     *
     * @param key 指定的键。
     */
    void delete(K key) throws CacheException;
}
