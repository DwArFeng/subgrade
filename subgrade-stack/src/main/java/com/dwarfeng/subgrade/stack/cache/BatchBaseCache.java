package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.CacheException;

import java.util.List;

/**
 * 批量基础缓存接口。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface BatchBaseCache<K extends Key, E extends Entity<K>> extends BaseCache<K, E> {

    /**
     * 查询指定的主键是否全部存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部存在。
     * @throws CacheException 缓存异常。
     */
    boolean allExists(List<K> keys) throws CacheException;

    /**
     * 查询指定的主键是否全部不存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部不存在。
     * @throws CacheException 缓存异常。
     */
    boolean nonExists(List<K> keys) throws CacheException;

    /**
     * 批量获取指定的键对应的元素。
     *
     * @param keys 指定的键组成的列表。
     * @return 指定的键对应的元素组成的列表。
     * @throws CacheException 缓存异常。
     */
    List<E> batchGet(List<K> keys) throws CacheException;

    /**
     * 向缓存中批量推送指定的键值对。
     * <p>需要保证 keys 和 entities 的数量一致。</p>
     *
     * @param entities 指定的值组成的列表。
     * @param timeout  超时时间。
     * @throws CacheException 缓存异常。
     */
    void batchPush(List<E> entities, long timeout) throws CacheException;

    /**
     * 批量删除指定的键对应的元素。
     *
     * @param keys 指定的键组成的列表。
     * @throws CacheException 缓存异常。
     */
    void batchDelete(List<K> keys) throws CacheException;
}
