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
import com.dwarfeng.subgrade.stack.service.EntityCrudService;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 通用的实体增删改查服务。
 * <p>该类同时使用数据访问层和缓存实现实体的增删改查方法。</p>
 * <p>插入没有主键的对象时，该服务会试图通过主键抓取器抓取新的主键，如果没有主键抓取器，就会报出异常。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class GeneralCrudService<K extends Key, E extends Entity<K>> implements EntityCrudService<K, E> {

    private BaseDao<K, E> dao;
    private BaseCache<K, E> cache;
    private KeyFetcher<K> keyFetcher;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;
    private long cacheTimeout;

    public GeneralCrudService(
            @NonNull BaseDao<K, E> dao,
            @NonNull BaseCache<K, E> cache,
            @NonNull KeyFetcher<K> keyFetcher,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel,
            @NonNull long cacheTimeout) {
        this.dao = dao;
        this.cache = cache;
        this.keyFetcher = keyFetcher;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        return innerExists(key);
    }

    private boolean innerExists(K key) throws ServiceException {
        try {
            if (cache.exists(key)) {
                return true;
            }
            return dao.exists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            if (cache.exists(key)) {
                return cache.get(key);
            }
            E entity = dao.get(key);
            cache.push(key, entity, cacheTimeout);
            return entity;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体信息时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insert(E element) throws ServiceException {
        try {
            if (innerExists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
            }

            if (Objects.isNull(element.getKey())) {
                element.setKey(keyFetcher.fetchKey());
            }
            dao.insert(element);
            cache.push(element.getKey(), element, cacheTimeout);
            return element.getKey();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K update(E element) throws ServiceException {
        try {
            if (!innerExists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            dao.update(element);
            cache.push(element.getKey(), element, cacheTimeout);
            return element.getKey();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            if (!innerExists(key)) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            if (cache.exists(key)) {
                cache.delete(key);
            }
            dao.delete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
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
            cache.push(key, entity, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("将实体存入缓存时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public BaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NonNull BaseDao<K, E> dao) {
        this.dao = dao;
    }

    public BaseCache<K, E> getCache() {
        return cache;
    }

    public void setCache(@NonNull BaseCache<K, E> cache) {
        this.cache = cache;
    }

    public KeyFetcher<K> getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(@NonNull KeyFetcher<K> keyFetcher) {
        this.keyFetcher = keyFetcher;
    }

    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@NonNull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@NonNull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
    }

    public long getCacheTimeout() {
        return cacheTimeout;
    }

    public void setCacheTimeout(@NonNull long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }
}
