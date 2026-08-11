package com.dwarfeng.subgrade.data.impl.dao.memory;

import com.dwarfeng.dutil.basic.stack.model.ReferenceModel;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.internal.bean.BeanPropertyAccess;
import com.dwarfeng.subgrade.data.sdk.memory.io.RefResourceBridge;
import com.dwarfeng.subgrade.data.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;

import org.jetbrains.annotations.NotNull;

/**
 * 通过内存引用模型实现的单对象数据访问层。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * <p>
 * 该类可以通过 RefResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。<br>
 * 如果该数据访问层单独使用，可以利用 {@link #fillData(RefResourceBridge)} 初始化内存，
 * 或利用 {@link #saveData(RefResourceBridge)} 保存内存数据。<br>
 * 如果该数据访问层和其它数据访问层组合使用，则需要和其它数据访问层共享 memory，
 * 此时宜在外部持有 memory 的引用，并在外部初始化或是保存。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class MemorySingleObjectDao<K extends Key, E extends Entity<K>> implements SingleObjectDao<E> {

    private ReferenceModel<E> memory;
    private K key;

    public MemorySingleObjectDao(
            @NotNull ReferenceModel<E> memory,
            @NotNull K key
    ) {
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
            E newEntity = BeanPropertyAccess.getInstance().cloneBean(entity);
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

    /**
     * 填充数据。
     *
     * @param rrb 引用资源桥。
     * @throws DaoException 数据访问层异常。
     */
    public void fillData(@NotNull RefResourceBridge<K, E> rrb) throws DaoException {
        try {
            rrb.fillRef(this.memory);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * 保存数据。
     *
     * @param rrb 引用资源桥。
     * @throws DaoException 数据访问层异常。
     */
    public void saveData(@NotNull RefResourceBridge<K, E> rrb) throws DaoException {
        try {
            rrb.saveRef(this.memory);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public ReferenceModel<E> getMemory() {
        return memory;
    }

    public void setMemory(@NotNull ReferenceModel<E> memory) {
        this.memory = memory;
    }

    public K getKey() {
        return key;
    }

    public void setKey(@NotNull K key) {
        this.key = key;
    }
}
