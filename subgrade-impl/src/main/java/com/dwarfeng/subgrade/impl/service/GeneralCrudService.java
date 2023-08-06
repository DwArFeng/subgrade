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
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.CrudService;

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
public class GeneralCrudService<K extends Key, E extends Entity<K>> implements CrudService<K, E> {

    @Nonnull
    private BaseDao<K, E> dao;
    @Nonnull
    private BaseCache<K, E> cache;
    @Nonnull
    private KeyFetcher<K> keyFetcher;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;
    @Nonnegative
    private long cacheTimeout;

    public GeneralCrudService(
            @Nonnull BaseDao<K, E> dao,
            @Nonnull BaseCache<K, E> cache,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        this.dao = dao;
        this.cache = cache;
        this.keyFetcher = keyFetcher;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private boolean internalExists(K key) throws Exception {
        if (cache.exists(key)) {
            return true;
        }
        return dao.exists(key);
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体信息时发生异常", exceptionLogLevel, sem, e);
        }
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
    public K insert(E element) throws ServiceException {
        try {
            return internalInsert(element);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private K internalInsert(E element) throws Exception {
        if (Objects.isNull(element.getKey())) {
            element.setKey(keyFetcher.fetchKey());
        } else if (internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }
        K key = dao.insert(element);
        cache.push(element, cacheTimeout);
        return key;
    }

    @Override
    public void update(E element) throws ServiceException {
        try {
            internalUpdate(element);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private void internalUpdate(E element) throws Exception {
        if (!internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.update(element);
        cache.push(element, cacheTimeout);
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("获取实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insertIfNotExists(E element) throws ServiceException {
        try {
            if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                return internalInsert(element);
            }
            return null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void updateIfExists(E element) throws ServiceException {
        try {
            if (internalExists(element.getKey())) {
                internalUpdate(element);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void deleteIfExists(K key) throws ServiceException {
        try {
            if (internalExists(key)) {
                internalDelete(key);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insertOrUpdate(E element) throws ServiceException {
        try {
            if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                return internalInsert(element);
            } else {
                internalUpdate(element);
                return null;
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入或更新实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("将实体存入缓存时发生异常", exceptionLogLevel, sem, e);
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
    public KeyFetcher<K> getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(@Nonnull KeyFetcher<K> keyFetcher) {
        this.keyFetcher = keyFetcher;
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
        return "GeneralCrudService{" +
                "dao=" + dao +
                ", cache=" + cache +
                ", keyFetcher=" + keyFetcher +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                ", cacheTimeout=" + cacheTimeout +
                '}';
    }
}
