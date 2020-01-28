package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通过内存实现的基础数据访问层。
 * <p>该类只提供最基本的方法实现，没有添加同步锁或其它的安全性一致性保证，请通过代理的方式在代理类中添加。</p>
 * <p>该类可以通过 MapResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。</p>
 *
 * @author DwArFeng
 * @see com.dwarfeng.subgrade.sdk.io.MapResourceBridge
 * @since 0.0.3-beta
 */
public class MemoryBatchBaseDao<K extends Key, E extends Entity<K>> implements BatchBaseDao<K, E> {

    private Map<K, E> memory;

    public MemoryBatchBaseDao(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }

    @Override
    public K insert(E element) throws DaoException {
        K key = element.getKey();
        if (memory.containsKey(key)) {
            throw new DaoException("元素已经存在: " + key);
        }
        memory.put(key, element);
        return key;
    }

    @Override
    public K update(E element) throws DaoException {
        K key = element.getKey();
        if (!memory.containsKey(key)) {
            throw new DaoException("元素不存在: " + key);
        }
        memory.put(key, element);
        return key;
    }

    @Override
    public void delete(K key) throws DaoException {
        if (!memory.containsKey(key)) {
            throw new DaoException("元素不存在: " + key);
        }
        memory.remove(key);
    }

    @Override
    public boolean exists(K key) {
        return memory.containsKey(key);
    }

    @Override
    public E get(K key) throws DaoException {
        if (!memory.containsKey(key)) {
            throw new DaoException("元素不存在: " + key);
        }
        return memory.get(key);
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws DaoException {
        List<K> collect = elements.stream().map(E::getKey).collect(Collectors.toList());
        Map<K, E> map = elements.stream().collect(Collectors.toMap(E::getKey, Function.identity()));
        if (!internalNonExists(collect)) {
            throw new DaoException("至少一个元素存在");
        }
        memory.putAll(map);
        return collect;
    }

    @Override
    public List<K> batchUpdate(List<E> elements) throws DaoException {
        List<K> collect = elements.stream().map(E::getKey).collect(Collectors.toList());
        Map<K, E> map = elements.stream().collect(Collectors.toMap(E::getKey, Function.identity()));
        if (!internalAllExists(collect)) {
            throw new DaoException("至少一个元素不存在");
        }
        memory.putAll(map);
        return collect;
    }

    @Override
    public void batchDelete(List<K> keys) {
        memory.keySet().removeAll(keys);
    }

    @Override
    public boolean allExists(List<K> keys) {
        return internalAllExists(keys);
    }

    private boolean internalAllExists(Collection<K> keys) {
        for (K key : keys) {
            if (!memory.containsKey(key)) return false;
        }
        return true;
    }

    @Override
    public boolean nonExists(List<K> keys) {
        return internalNonExists(keys);
    }

    private boolean internalNonExists(Collection<K> keys) {
        for (K key : keys) {
            if (memory.containsKey(key)) return false;
        }
        return true;
    }

    @Override
    public List<E> batchGet(List<K> keys) {
        List<E> elements = new ArrayList<>();
        for (K key : keys) {
            elements.add(memory.get(key));
        }
        return elements;
    }

    public Map<K, E> getMemory() {
        return memory;
    }

    public void setMemory(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }
}
