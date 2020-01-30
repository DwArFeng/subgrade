package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchCrudService;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 通用的实体增删改查服务。
 * <p>该类同时使用数据访问层和缓存实现实体的增删改查方法。</p>
 * <p>插入没有主键的对象时，该服务会试图通过主键抓取器抓取新的主键，如果没有主键抓取器，就会报出异常。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class GeneralBatchCrudService<K extends Key, E extends Entity<K>> implements BatchCrudService<K, E> {

    private BatchBaseDao<K, E> dao;
    private BatchBaseCache<K, E> cache;
    private KeyFetcher<K> keyFetcher;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;
    private long cacheTimeout;

    public GeneralBatchCrudService(
            @NonNull BatchBaseDao<K, E> dao,
            @NonNull BatchBaseCache<K, E> cache,
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
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体信息时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insert(E element) throws ServiceException {
        try {
            if (Objects.nonNull(element.getKey()) && internalExists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
            }

            if (Objects.isNull(element.getKey())) {
                element.setKey(keyFetcher.fetchKey());
            }
            K key = dao.insert(element);
            cache.push(element, cacheTimeout);
            return key;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void update(E element) throws ServiceException {
        try {
            if (!internalExists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            dao.update(element);
            cache.push(element, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            if (!internalExists(key)) {
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
        E entity = dao.get(key);
        cache.push(entity, cacheTimeout);
        return entity;
    }

    @Override
    public boolean allExists(List<K> keys) throws ServiceException {
        try {
            return internalAllExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
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
            List<E> elements = new ArrayList<>();
            for (K key : keys) {
                elements.add(internalGet(key));
            }
            return elements;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws ServiceException {
        try {
            List<K> collect = elements.stream().filter(e -> Objects.nonNull(e.getKey())).map(E::getKey).collect(Collectors.toList());
            if (internalNonExists(collect)) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
            }

            for (E element : elements) {
                if (Objects.isNull(element.getKey())) {
                    element.setKey(keyFetcher.fetchKey());
                }
            }

            List<K> ks = dao.batchInsert(elements);
            cache.batchPush(elements, cacheTimeout);
            return ks;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void batchUpdate(List<E> elements) throws ServiceException {
        try {
            List<K> collect = elements.stream().map(E::getKey).collect(Collectors.toList());
            if (!internalAllExists(collect)) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            dao.batchUpdate(elements);
            cache.batchPush(elements, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws ServiceException {
        try {
            if (!internalAllExists(keys)) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            cache.batchDelete(keys);
            dao.batchDelete(keys);
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
            internalDumpCache(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("将实体存入缓存时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("将实体存入缓存时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public BatchBaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NonNull BatchBaseDao<K, E> dao) {
        this.dao = dao;
    }

    public BatchBaseCache<K, E> getCache() {
        return cache;
    }

    public void setCache(@NonNull BatchBaseCache<K, E> cache) {
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
