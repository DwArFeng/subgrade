package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.GenerateException;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class GeneralBatchCrudService<K extends Key, E extends Entity<K>> extends AbstractBatchCrudService<K, E> {

    @Nonnull
    private BatchBaseDao<K, E> dao;

    @Nonnull
    private BatchBaseCache<K, E> cache;

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
    public GeneralBatchCrudService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull BatchBaseDao<K, E> dao,
            @Nonnull BatchBaseCache<K, E> cache,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
        this.cacheTimeout = cacheTimeout;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #GeneralBatchCrudService(ServiceExceptionMapper, LogLevel, BatchBaseDao, BatchBaseCache, KeyGenerator, long)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               基础数据访问层。
     * @param cache             基础缓存接口。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param cacheTimeout      缓存超时时间。
     * @see #GeneralBatchCrudService(ServiceExceptionMapper, LogLevel, BatchBaseDao, BatchBaseCache, KeyGenerator, long)
     * @deprecated 使用 {@link #GeneralBatchCrudService(ServiceExceptionMapper, LogLevel, BatchBaseDao, BatchBaseCache, KeyGenerator, long)} 代替。
     */
    @Deprecated
    public GeneralBatchCrudService(
            @Nonnull BatchBaseDao<K, E> dao,
            @Nonnull BatchBaseCache<K, E> cache,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
        this.cacheTimeout = cacheTimeout;
    }

    @Deprecated
    public GeneralBatchCrudService(
            @Nonnull BatchBaseDao<K, E> dao,
            @Nonnull BatchBaseCache<K, E> cache,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
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

    @Override
    protected boolean doAllExists(List<K> keys) throws Exception {
        return internalAllExists(keys);
    }

    private boolean internalAllExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (!internalExists(key)) return false;
        }
        return true;
    }

    @Override
    protected boolean doNonExists(List<K> keys) throws Exception {
        return internalNonExists(keys);
    }

    private boolean internalNonExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (internalExists(key)) return false;
        }
        return true;
    }

    @Override
    protected List<E> doBatchGet(List<K> keys) throws Exception {
        return internalBatchGet(keys);
    }

    private List<E> internalBatchGet(List<K> keys) throws Exception {
        List<E> entities = new ArrayList<>();
        for (K key : keys) {
            entities.add(internalGet(key));
        }
        return entities;
    }

    @Override
    protected List<K> doBatchInsert(List<E> entities) throws Exception {
        return internalBatchInsert(entities);
    }

    private List<K> internalBatchInsert(List<E> entities) throws Exception {
        List<K> collect = entities.stream().filter(
                e -> Objects.nonNull(e.getKey())).map(E::getKey).collect(Collectors.toList()
        );
        if (!internalNonExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }

        List<E> nonKeyEntities = entities.stream().filter(e -> Objects.isNull(e.getKey())).collect(Collectors.toList());
        // 根据 nonKeyEntities 的大小，选择性生成主键。
        mayGenerateKeys(nonKeyEntities);

        List<K> ks = dao.batchInsert(entities);
        cache.batchPush(entities, cacheTimeout);
        return ks;
    }

    private void mayGenerateKeys(List<E> nonKeyEntities) throws GenerateException {
        // 如果 nonKeyEntities 为空，则不生成主键。
        if (nonKeyEntities.isEmpty()) {
            return;
        }
        // 否则生成主键。
        List<K> generatedKeys = keyGenerator.batchGenerate(nonKeyEntities.size());
        for (int i = 0; i < nonKeyEntities.size(); i++) {
            nonKeyEntities.get(i).setKey(generatedKeys.get(i));
        }
    }

    @Override
    protected void doBatchUpdate(List<E> entities) throws Exception {
        internalBatchUpdate(entities);
    }

    private void internalBatchUpdate(List<E> entities) throws Exception {
        List<K> collect = entities.stream().map(E::getKey).collect(Collectors.toList());
        if (!internalAllExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.batchUpdate(entities);
        cache.batchPush(entities, cacheTimeout);
    }

    @Override
    protected void doBatchDelete(List<K> keys) throws Exception {
        internalBatchDelete(keys);
    }

    private void internalBatchDelete(List<K> keys) throws Exception {
        if (!internalAllExists(keys)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        cache.batchDelete(keys);
        dao.batchDelete(keys);
    }

    @Override
    protected List<E> doBatchGetIfExists(List<K> keys) throws Exception {
        List<K> existsKeys = new ArrayList<>();
        for (K key : keys) {
            if (internalExists(key)) {
                existsKeys.add(key);
            }
        }
        return internalBatchGet(existsKeys);
    }

    @Override
    protected List<K> doBatchInsertIfNotExists(List<E> entities) throws Exception {
        List<E> entities2Insert = new ArrayList<>();
        for (E entity : entities) {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                entities2Insert.add(entity);
            }
        }
        return internalBatchInsert(entities2Insert);
    }

    @Override
    protected void doBatchUpdateIfExists(List<E> entities) throws Exception {
        List<E> entities2Update = new ArrayList<>();
        for (E entity : entities) {
            if (internalExists(entity.getKey())) {
                entities2Update.add(entity);
            }
        }
        internalBatchUpdate(entities2Update);
    }

    @Override
    protected void doBatchDeleteIfExists(List<K> keys) throws Exception {
        List<K> keys2Delete = new ArrayList<>();
        for (K key : keys) {
            if (internalExists(key)) {
                keys2Delete.add(key);
            }
        }
        internalBatchDelete(keys2Delete);
    }

    @Override
    protected List<K> doBatchInsertOrUpdate(List<E> entities) throws Exception {
        List<E> entities2Insert = new ArrayList<>();
        List<E> entities2Update = new ArrayList<>();
        for (E entity : entities) {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                entities2Insert.add(entity);
            } else {
                entities2Update.add(entity);
            }
        }
        internalBatchUpdate(entities2Update);
        return internalBatchInsert(entities2Insert);
    }

    /**
     * 将指定的键对应的实体存入缓存中。
     *
     * @param key 指定的键。
     * @throws ServiceException 服务异常。
     */
    public void dumpCache(K key) throws ServiceException {
        try {
            internalDumpCache(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("将实体存入缓存时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalDumpCache(K key) throws Exception {
        E entity;
        if (cache.exists(key)) {
            entity = cache.get(key);
        } else {
            entity = dao.get(key);
        }
        cache.push(entity, cacheTimeout);
    }

    /**
     * 将指定的键组成的集合对应的实体批量存入缓存中。
     *
     * @param keys 指定的键组成的集合。
     * @throws ServiceException 服务异常。
     */
    public void batchDumpCache(List<K> keys) throws ServiceException {
        try {
            for (K key : keys) {
                internalDumpCache(key);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("将实体存入缓存时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public BatchBaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull BatchBaseDao<K, E> dao) {
        this.dao = dao;
    }

    @Nonnull
    public BatchBaseCache<K, E> getCache() {
        return cache;
    }

    public void setCache(@Nonnull BatchBaseCache<K, E> cache) {
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
        return "GeneralBatchCrudService{" +
                "dao=" + dao +
                ", cache=" + cache +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                ", cacheTimeout=" + cacheTimeout +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
