package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.SingleObjectCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;

/**
 * 使用 Null（空对象）实现的 SingleObjectCache。
 *
 * <p>
 * 目的：为不需要缓存的小型项目提供一种在不改业务代码的情况下禁用缓存的方式。
 * 该实现遵循 Null Object 模式：
 * <ul>
 * <li>exists 方法永远返回 false；</li>
 * <li>读取方法（如 get）永远抛出 {@link CacheException}；</li>
 * <li>写入/清理等方法为空操作，不产生任何副作用。</li>
 * </ul>
 *
 * <p>
 * 用法：在 Spring 配置中，将原有的 Redis 缓存 Bean 替换为本实现，即可在不修改业务代码的前提下禁用缓存逻辑。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public class NullSingleObjectCache<E extends Entity<?>> implements SingleObjectCache<E> {

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public E get() throws CacheException {
        throw new CacheException("Null cache does not support get operation");
    }

    @Override
    public void put(E entity, long timeout) {
    }

    @Override
    public void clear() {
    }
}
