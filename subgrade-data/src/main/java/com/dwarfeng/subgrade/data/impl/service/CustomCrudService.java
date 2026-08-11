package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.sdk.exception.ServiceExceptionCodeSuppliers;
import com.dwarfeng.subgrade.data.sdk.service.AbstractCrudService;
import com.dwarfeng.subgrade.data.sdk.service.custom.operation.CrudOperation;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * 自定义的实体增删改查服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务或同步锁，请通过代理的方式在代理类中添加事务或者同步锁。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public class CustomCrudService<K extends Key, E extends Entity<K>> extends AbstractCrudService<K, E> {

    @NotNull
    private CrudOperation<K, E> operation;

    @NotNull
    private KeyGenerator<K> keyGenerator;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param operation         服务的增删改查操作。
     * @param keyGenerator      主键生成器。
     * @since 1.5.4
     */
    public CustomCrudService(
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel,
            @NotNull CrudOperation<K, E> operation,
            @NotNull KeyGenerator<K> keyGenerator
    ) {
        super(sem, exceptionLogLevel);
        this.operation = operation;
        this.keyGenerator = keyGenerator;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #CustomCrudService(ServiceExceptionMapper, LogLevel, CrudOperation, KeyGenerator)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param operation         服务的增删改查操作。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #CustomCrudService(ServiceExceptionMapper, LogLevel, CrudOperation, KeyGenerator)
     * @deprecated 使用 {@link #CustomCrudService(ServiceExceptionMapper, LogLevel, CrudOperation, KeyGenerator)} 代替。
     */
    @Deprecated
    public CustomCrudService(
            @NotNull CrudOperation<K, E> operation,
            @NotNull KeyGenerator<K> keyGenerator,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.operation = operation;
        this.keyGenerator = keyGenerator;
    }

    @Override
    protected boolean doExists(K key) throws Exception {
        return internalExists(key);
    }

    private boolean internalExists(K key) throws Exception {
        return operation.exists(key);
    }

    @Override
    protected E doGet(K key) throws Exception {
        return internalGet(key);
    }

    private E internalGet(K key) throws Exception {
        if (!operation.exists(key)) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }
        return operation.get(key);
    }

    @Override
    protected K doInsert(E entity) throws Exception {
        return internalInsert(entity);
    }

    private K internalInsert(E entity) throws Exception {
        if (Objects.isNull(entity.getKey())) {
            entity.setKey(keyGenerator.generate());
        }
        if (internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_EXISTED.get());
        }
        return operation.insert(entity);
    }

    @Override
    protected void doUpdate(E entity) throws Exception {
        internalUpdate(entity);
    }

    private void internalUpdate(E entity) throws Exception {
        if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }
        operation.update(entity);
    }

    @Override
    protected void doDelete(K key) throws Exception {
        internalDelete(key);
    }

    private void internalDelete(K key) throws Exception {
        if (Objects.isNull(key) || !internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }
        operation.delete(key);
    }

    @Override
    protected E doGetIfExists(K key) throws Exception {
        return internalExists(key) ? internalGet(key) : null;
    }

    @Override
    protected K doInsertIfNotExists(E entity) throws Exception {
        if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
            return internalInsert(entity);
        }
        return null;
    }

    @Override
    protected void doUpdateIfExists(E entity) throws Exception {
        if (internalExists(entity.getKey())) {
            internalUpdate(entity);
        }
    }

    @Override
    protected void doDeleteIfExists(K key) throws Exception {
        if (internalExists(key)) {
            internalDelete(key);
        }
    }

    @Override
    protected K doInsertOrUpdate(E entity) throws Exception {
        if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
            return internalInsert(entity);
        } else {
            internalUpdate(entity);
            return null;
        }
    }

    @NotNull
    public CrudOperation<K, E> getOperation() {
        return operation;
    }

    public void setOperation(@NotNull CrudOperation<K, E> operation) {
        this.operation = operation;
    }

    @NotNull
    public KeyGenerator<K> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(@NotNull KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public String toString() {
        return "CustomCrudService{" +
                "operation=" + operation +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
