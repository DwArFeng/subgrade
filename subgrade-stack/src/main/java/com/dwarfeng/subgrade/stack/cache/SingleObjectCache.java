package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.CacheException;

/**
 * 单对象缓存。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public interface SingleObjectCache<E extends Entity<?>> extends Cache<E> {

    /**
     * 判断单个实体是否存在。
     *
     * @return 单个实体是否存在。
     * @throws CacheException 数据访问层异常。
     */
    boolean exists() throws CacheException;

    /**
     * 获取单个对象。
     *
     * @return 单个对象的实体。
     * @throws CacheException 服务异常。
     */
    E get() throws CacheException;

    /**
     * 插入或更新指定的实体。
     *
     * @param entity  指定的实体。
     * @param timeout 超时时间。
     * @throws CacheException 服务异常。
     */
    void put(E entity, long timeout) throws CacheException;
}
