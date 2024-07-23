package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.cache.BaseCache;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 通用的实体增删改查服务。
 *
 * <p>
 * 该类同时使用数据访问层和缓存实现实体的增删改查方法。
 *
 * <p>
 * 插入没有主键的对象时，该服务会试图通过主键抓取器抓取新的主键，如果没有主键抓取器，就会报出异常。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class GeneralCrudService<K extends Key, E extends Entity<K>> extends AbstractCrudService<K, E> {

    @Nonnull
    private BaseDao<K, E> dao;

    @Nonnull
    private BaseCache<K, E> cache;

    @Nonnull
    private KeyGenerator<K> keyGenerator;

    @Nonnegative
    private long cacheTimeout;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               基础数据访问层。
     * @param cache             基础缓存接口。
     * @param keyGenerator      主键生成器。
     * @param cacheTimeout      缓存超时时间。
     * @since 1.5.4
     */
    public GeneralCrudService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull BaseDao<K, E> dao,
            @Nonnull BaseCache<K, E> cache,
            @Nonnull KeyGenerator<K> keyGenerator,
            long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #GeneralCrudService(ServiceExceptionMapper, LogLevel, BaseDao, BaseCache, KeyGenerator, long)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               基础数据访问层。
     * @param cache             基础缓存接口。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param cacheTimeout      缓存超时时间。
     * @see #GeneralCrudService(ServiceExceptionMapper, LogLevel, BaseDao, BaseCache, KeyGenerator, long)
     * @deprecated 使用 {@link #GeneralCrudService(ServiceExceptionMapper, LogLevel, BaseDao, BaseCache, KeyGenerator, long)} 代替。
     */
    @Deprecated
    public GeneralCrudService(
            @Nonnull BaseDao<K, E> dao,
            @Nonnull BaseCache<K, E> cache,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Deprecated
    public GeneralCrudService(
            @Nonnull BaseDao<K, E> dao,
            @Nonnull BaseCache<K, E> cache,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    protected boolean doExists(K key) throws Exception {
        return internalExists(key);
    }

    private boolean internalExists(K key) throws Exception {
        if (cache.exists(key)) {
            return true;
        }
        return dao.exists(key);
    }

    @Override
    protected E doGet(K key) throws Exception {
        return internalGet(key);
    }

    private E internalGet(K key) throws Exception {
        if (cache.exists(key)) {
            return cache.get(key);
        }
        if (!dao.exists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        E entity = dao.get(key);
        cache.push(entity, cacheTimeout);
        return entity;
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
        K key = dao.insert(entity);
        cache.push(entity, cacheTimeout);
        return key;
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
        cache.push(entity, cacheTimeout);
    }

    @Override
    protected void doDelete(K key) throws Exception {
        internalDelete(key);
    }

    private void internalDelete(K key) throws Exception {
        if (!internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        if (cache.exists(key)) {
            cache.delete(key);
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

    /**
     * 将指定的键对应的实体存入缓存中。
     *
     * @param key 指定的键。
     * @throws ServiceException 服务异常。
     */
    public void dumpCache(K key) throws ServiceException {
        try {
            E entity;
            if (cache.exists(key)) {
                entity = cache.get(key);
            } else {
                entity = dao.get(key);
            }
            cache.push(entity, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("将实体存入缓存时发生异常", exceptionLogLevel, e, sem);
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
    public BaseCache<K, E> getCache() {
        return cache;
    }

    public void setCache(@Nonnull BaseCache<K, E> cache) {
        this.cache = cache;
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

    public long getCacheTimeout() {
        return cacheTimeout;
    }

    public void setCacheTimeout(long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public String toString() {
        return "GeneralCrudService{" +
                "dao=" + dao +
                ", cache=" + cache +
                ", keyGenerator=" + keyGenerator +
                ", cacheTimeout=" + cacheTimeout +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
