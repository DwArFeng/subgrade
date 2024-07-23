package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;

import javax.annotation.Nonnull;
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
@SuppressWarnings("DuplicatedCode")
public class DaoOnlyCrudService<K extends Key, E extends Entity<K>> extends AbstractCrudService<K, E> {

    @Nonnull
    private BaseDao<K, E> dao;

    @Nonnull
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
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull BaseDao<K, E> dao,
            @Nonnull KeyGenerator<K> keyGenerator
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
            @Nonnull BaseDao<K, E> dao,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    @Deprecated
    public DaoOnlyCrudService(
            @Nonnull BaseDao<K, E> dao,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
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
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
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
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }
        return dao.insert(entity);
    }

    @Override
    protected void doUpdate(E entity) throws Exception {
        internalUpdate(entity);
    }

    private void internalUpdate(E entity) throws Exception {
        if (!internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.update(entity);
    }

    @Override
    protected void doDelete(K key) throws Exception {
        internalDelete(key);
    }

    private void internalDelete(K key) throws Exception {
        if (!internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
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

    @Nonnull
    public BaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull BaseDao<K, E> dao) {
        this.dao = dao;
    }

    @Nonnull
    public KeyGenerator<K> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(@Nonnull KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Deprecated
    @Nonnull
    public KeyFetcher<K> getKeyFetcher() {
        return KeyFetcherAdaptHelper.toKeyFetcher(keyGenerator);
    }

    @Deprecated
    public void setKeyFetcher(@Nonnull KeyFetcher<K> keyFetcher) {
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
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
