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
import com.dwarfeng.subgrade.stack.service.BatchCrudService;

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
public class GeneralBatchCrudService<K extends Key, E extends Entity<K>> implements BatchCrudService<K, E> {

    @Nonnull
    private BatchBaseDao<K, E> dao;
    @Nonnull
    private BatchBaseCache<K, E> cache;
    @Nonnull
    private KeyGenerator<K> keyGenerator;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;
    @Nonnegative
    private long cacheTimeout;

    public GeneralBatchCrudService(
            @Nonnull BatchBaseDao<K, E> dao,
            @Nonnull BatchBaseCache<K, E> cache,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            long cacheTimeout
    ) {
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = keyGenerator;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
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
        this.dao = dao;
        this.cache = cache;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体信息时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public K insert(E entity) throws ServiceException {
        try {
            return internalInsert(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
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
    public void update(E entity) throws ServiceException {
        try {
            internalUpdate(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalUpdate(E entity) throws Exception {
        if (!internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.update(entity);
        cache.push(entity, cacheTimeout);
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
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
    public E getIfExists(K key) throws ServiceException {
        try {
            return internalExists(key) ? internalGet(key) : null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public K insertIfNotExists(E entity) throws ServiceException {
        try {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                return internalInsert(entity);
            }
            return null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void updateIfExists(E entity) throws ServiceException {
        try {
            if (internalExists(entity.getKey())) {
                internalUpdate(entity);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void deleteIfExists(K key) throws ServiceException {
        try {
            if (internalExists(key)) {
                internalDelete(key);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public K insertOrUpdate(E entity) throws ServiceException {
        try {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                return internalInsert(entity);
            } else {
                internalUpdate(entity);
                return null;
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalExists(K key) throws Exception {
        if (cache.exists(key)) {
            return true;
        }
        return dao.exists(key);
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
    public boolean allExists(List<K> keys) throws ServiceException {
        try {
            return internalAllExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalAllExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (!internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public boolean nonExists(List<K> keys) throws ServiceException {
        try {
            return internalNonExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalNonExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public List<E> batchGet(List<K> keys) throws ServiceException {
        try {
            return internalBatchGet(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private List<E> internalBatchGet(List<K> keys) throws Exception {
        List<E> entities = new ArrayList<>();
        for (K key : keys) {
            entities.add(internalGet(key));
        }
        return entities;
    }

    @Override
    public List<K> batchInsert(List<E> entities) throws ServiceException {
        try {
            return internalBatchInsert(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
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
    public void batchUpdate(List<E> entities) throws ServiceException {
        try {
            internalBatchUpdate(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
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
    public void batchDelete(List<K> keys) throws ServiceException {
        try {
            internalBatchDelete(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalBatchDelete(List<K> keys) throws Exception {
        if (!internalAllExists(keys)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        cache.batchDelete(keys);
        dao.batchDelete(keys);
    }

    @Override
    public List<E> batchGetIfExists(List<K> keys) throws ServiceException {
        try {
            List<K> existsKeys = new ArrayList<>();
            for (K key : keys) {
                if (internalExists(key)) {
                    existsKeys.add(key);
                }
            }
            return internalBatchGet(existsKeys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public List<K> batchInsertIfExists(List<E> entities) throws ServiceException {
        try {
            List<E> entities2Insert = new ArrayList<>();
            for (E entity : entities) {
                if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                    entities2Insert.add(entity);
                }
            }
            return internalBatchInsert(entities2Insert);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public List<K> batchInsertIfNotExists(List<E> entities) throws ServiceException {
        try {
            List<E> entities2Insert = new ArrayList<>();
            for (E entity : entities) {
                if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                    entities2Insert.add(entity);
                }
            }
            return internalBatchInsert(entities2Insert);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void batchUpdateIfExists(List<E> entities) throws ServiceException {
        try {
            List<E> entities2Update = new ArrayList<>();
            for (E entity : entities) {
                if (internalExists(entity.getKey())) {
                    entities2Update.add(entity);
                }
            }
            internalBatchUpdate(entities2Update);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void batchDeleteIfExists(List<K> keys) throws ServiceException {
        try {
            List<K> keys2Delete = new ArrayList<>();
            for (K key : keys) {
                if (internalExists(key)) {
                    keys2Delete.add(key);
                }
            }
            internalBatchDelete(keys2Delete);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public List<K> batchInsertOrUpdate(List<E> entities) throws ServiceException {
        try {
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
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
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

    @Nonnull
    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@Nonnull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    @Nonnull
    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@Nonnull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
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
                '}';
    }
}
