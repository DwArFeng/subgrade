package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.lang.NonNull;

/**
 * 通过内存引用模型实现的单对象数据访问层。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class MemorySingleObjectDao<K extends Key, E extends Entity<K>> implements SingleObjectDao<E> {

    private ReferenceModel<E> memory;
    private K key;

    public MemorySingleObjectDao(
            @NonNull ReferenceModel<E> memory,
            @NonNull K key) {
        this.memory = memory;
        this.key = key;
    }

    @Override
    public boolean exists() {
        return internalExists();
    }

    private boolean internalExists() {
        return !memory.isEmpty();
    }

    @Override
    public E get() {
        return memory.get();
    }

    @Override
    public void put(E entity) throws DaoException {
        try {
            @SuppressWarnings("unchecked")
            E newEntity = (E) BeanUtils.cloneBean(entity);
            newEntity.setKey(key);
            memory.set(newEntity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void clear() throws DaoException {
        if (!internalExists()) {
            throw new DaoException("实体不存在");
        }
        memory.clear();
    }

    public ReferenceModel<E> getMemory() {
        return memory;
    }

    public void setMemory(@NonNull ReferenceModel<E> memory) {
        this.memory = memory;
    }

    public K getKey() {
        return key;
    }

    public void setKey(@NonNull K key) {
        this.key = key;
    }
}
