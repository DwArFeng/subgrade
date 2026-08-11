package com.dwarfeng.subgrade.data.stack.cache;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.stack.exception.CacheException;

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
