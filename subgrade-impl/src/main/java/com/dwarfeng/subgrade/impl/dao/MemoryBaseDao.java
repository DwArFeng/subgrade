package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.memory.io.MapResourceBridge;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过内存实现的基础数据访问层。
 *
 * <p>该类只提供最基本的方法实现，没有添加同步锁或其它的安全性一致性保证，请通过代理的方式在代理类中添加。</p>
 * <p>该类可以通过 MapResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。</p>
 *
 * @author DwArFeng
 * @see MapResourceBridge
 * @since 0.0.3-beta
 */
public class MemoryBaseDao<K extends Key, E extends Entity<K>> implements BaseDao<K, E> {

    private Map<K, E> memory;

    public MemoryBaseDao() {
        this(new HashMap<>());
    }

    public MemoryBaseDao(@NonNull Map<K, E> memory) {
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
    public void update(E element) throws DaoException {
        K key = element.getKey();
        if (!memory.containsKey(key)) {
            throw new DaoException("元素不存在: " + key);
        }
        memory.put(key, element);
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

    /**
     * 填充数据。
     *
     * @param mrb 映射资源桥。
     * @throws DaoException 数据访问层异常。
     */
    public void fillData(@NonNull MapResourceBridge<K, E> mrb) throws DaoException {
        try {
            mrb.fillMap(this.memory);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 保存数据。
     *
     * @param mrb 映射资源桥。
     * @throws DaoException 数据访问层异常。
     */
    public void saveData(@NonNull MapResourceBridge<K, E> mrb) throws DaoException {
        try {
            mrb.saveMap(this.memory);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Map<K, E> getMemory() {
        return memory;
    }

    public void setMemory(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }
}
