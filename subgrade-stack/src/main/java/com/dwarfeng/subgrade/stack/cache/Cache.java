package com.dwarfeng.subgrade.stack.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.CacheException;

/**
 * 缓存抽象接口。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Cache<E extends Entity<?>> {

    /**
     * 清空缓存内的所有内容。
     *
     * @throws CacheException 缓存异常。
     */
    void clear() throws CacheException;
}
