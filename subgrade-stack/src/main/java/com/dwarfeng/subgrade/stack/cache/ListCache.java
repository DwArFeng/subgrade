package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.CacheException;

import java.util.Collection;
import java.util.List;

/**
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface ListCache<E extends Entity<?>> extends Cache<E> {

    /**
     * 获取缓存中指定的键是否存在。
     *
     * @return 指定的键是否存在。
     * @throws CacheException 缓存异常。
     */
    boolean exists() throws CacheException;

    /**
     * 获取缓存中的元素数量。
     *
     * @return 缓存中的元素数量。
     * @throws CacheException 缓存异常。
     */
    int size() throws CacheException;

    /**
     * 获取缓存中指定键对应的实体列表。
     *
     * @return 指定的键对应的实体列表。
     * @throws CacheException 缓存异常。
     */
    List<E> get() throws CacheException;

    /**
     * 获取缓存中指定的键对应的实体列表的子列表。
     *
     * @param beginIndex 子列表的起始元素。
     * @param maxEntity  子列表的最大元素数量。
     * @return 指定的键对应的实体列表的子列表。
     * @throws CacheException 缓存异常。
     */
    List<E> get(int beginIndex, int maxEntity) throws CacheException;

    /**
     * 获取缓存中指定的分页信息对应的实体列表的子列表。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 缓存中指定的分页信息对应的实体列表的子列表。
     * @throws CacheException 缓存异常。
     */
    List<E> get(PagingInfo pagingInfo) throws CacheException;

    /**
     * 将列表设置为指定的集合。
     *
     * @param entities 指定的实体集合。
     * @param timeout  超时时间。
     * @throws CacheException 缓存异常。
     */
    void set(Collection<E> entities, long timeout) throws CacheException;

    /**
     * 向列表的左侧推入指定的元素集合。
     *
     * @param entities 指定的实体元素集合。
     * @param timeout  超时时间。
     * @throws CacheException 缓存异常。
     */
    void leftPush(Collection<E> entities, long timeout) throws CacheException;

    /**
     * 向列表的右侧推入指定的元素集合。
     *
     * @param entities 指定的实体元素集合。
     * @param timeout  超时时间。
     * @throws CacheException 缓存异常。
     */
    void rightPush(Collection<E> entities, long timeout) throws CacheException;
}
