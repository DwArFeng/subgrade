package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;

import java.util.List;

/**
 * 使用 Null（空对象）实现的 BatchBaseCache。
 *
 * <p>
 * 目的：为不需要缓存的小型项目提供一种在不改业务代码的情况下禁用缓存的方式。
 * 该实现遵循 Null Object 模式：
 * <ul>
 * <li>exists/allExists 永远返回 false；nonExists 永远返回 true；</li>
 * <li>读取方法（如 get/batchGet/size 等）永远抛出 {@link CacheException}；</li>
 * <li>写入/删除/清理等方法为空操作，不产生任何副作用。</li>
 * </ul>
 *
 * <p>
 * 用法：在 Spring 配置中，将原有的 Redis 缓存 Bean 替换为本实现，即可在不修改业务代码的前提下禁用缓存逻辑。
 *
 * @author DwArFeng
 * @since 1.7.1
 */
public class NullBatchBaseCache<K extends Key, E extends Entity<K>> implements BatchBaseCache<K, E> {

    @Override
    public boolean exists(K key) {
        return false;
    }

    @Override
    public E get(K key) throws CacheException {
        throw new CacheException("Null cache does not support get operation");
    }

    @Override
    public void push(E entity, long timeout) {
    }

    @Override
    public void delete(K key) {
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean allExists(List<K> keys) {
        return false;
    }

    @Override
    public boolean nonExists(List<K> keys) {
        return true;
    }

    @Override
    public List<E> batchGet(List<K> keys) throws CacheException {
        throw new CacheException("Null cache does not support batch get operation");
    }

    @Override
    public void batchPush(List<E> entities, long timeout) {
    }

    @Override
    public void batchDelete(List<K> keys) {
    }
}
