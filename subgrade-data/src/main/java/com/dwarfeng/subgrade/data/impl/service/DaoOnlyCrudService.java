package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.sdk.exception.ServiceExceptionCodeSuppliers;
import com.dwarfeng.subgrade.data.sdk.service.AbstractCrudService;
import com.dwarfeng.subgrade.data.stack.dao.BaseDao;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * 仅通过数据访问层实现的实体增删改查服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DaoOnlyCrudService<K extends Key, E extends Entity<K>> extends AbstractCrudService<K, E> {

    @NotNull
    private BaseDao<K, E> dao;

    @NotNull
    private KeyGenerator<K> keyGenerator;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               基础数据访问层。
     * @param keyGenerator      主键生成器。
     * @since 1.5.4
     */
    public DaoOnlyCrudService(
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel,
            @NotNull BaseDao<K, E> dao,
            @NotNull KeyGenerator<K> keyGenerator
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyCrudService(ServiceExceptionMapper, LogLevel, BaseDao, KeyGenerator)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               基础数据访问层。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyCrudService(ServiceExceptionMapper, LogLevel, BaseDao, KeyGenerator)
     * @deprecated 使用 {@link #DaoOnlyCrudService(ServiceExceptionMapper, LogLevel, BaseDao, KeyGenerator)} 代替。
     */
    @Deprecated
    public DaoOnlyCrudService(
            @NotNull BaseDao<K, E> dao,
            @NotNull KeyGenerator<K> keyGenerator,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    @Override
    protected boolean doExists(K key) throws Exception {
        return internalExists(key);
    }

    private boolean internalExists(K key) throws Exception {
        return dao.exists(key);
    }

    @Override
    protected E doGet(K key) throws Exception {
        return internalGet(key);
    }

    private E internalGet(K key) throws Exception {
        if (!dao.exists(key)) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }
        return dao.get(key);
    }

    @Override
    protected K doInsert(E entity) throws Exception {
        return internalInsert(entity);
    }

    private K internalInsert(E entity) throws Exception {
        if (Objects.isNull(entity.getKey())) {
            entity.setKey(keyGenerator.generate());
        } else if (internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_EXISTED.get());
        }
        return dao.insert(entity);
    }

    @Override
    protected void doUpdate(E entity) throws Exception {
        internalUpdate(entity);
    }

    private void internalUpdate(E entity) throws Exception {
        if (!internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }

        dao.update(entity);
    }

    @Override
    protected void doDelete(K key) throws Exception {
        internalDelete(key);
    }

    private void internalDelete(K key) throws Exception {
        if (!internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST.get());
        }
        dao.delete(key);
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
    public BaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NotNull BaseDao<K, E> dao) {
        this.dao = dao;
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
        return "DaoOnlyCrudService{" +
                "dao=" + dao +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
