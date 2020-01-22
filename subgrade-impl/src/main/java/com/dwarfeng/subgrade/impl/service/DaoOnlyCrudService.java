package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.EntityCrudService;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 仅用数据访问层实现的实体增删改查服务。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DaoOnlyCrudService<K extends Key, E extends Entity<K>> implements EntityCrudService<K, E> {

    private BaseDao<K, E> dao;
    private KeyFetcher<K> keyFetcher;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;
    private long cacheTimeout;

    public DaoOnlyCrudService(
            @NonNull BaseDao<K, E> dao,
            @NonNull KeyFetcher<K> keyFetcher,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel,
            @NonNull long cacheTimeout) {
        this.dao = dao;
        this.keyFetcher = keyFetcher;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return dao.exists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在是发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            return dao.get(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在是发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insert(E element) throws ServiceException {
        try {
            if (dao.exists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
            }

            if (Objects.isNull(element.getKey())) {
                element.setKey(keyFetcher.fetchKey());
            }
            dao.insert(element);
            return element.getKey();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在是发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K update(E element) throws ServiceException {
        try {
            if (!dao.exists(element.getKey())) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            dao.update(element);
            return element.getKey();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在是发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            if (!dao.exists(key)) {
                throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
            }

            dao.delete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在是发生异常", exceptionLogLevel, sem, e);
        }
    }

    public BaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NonNull BaseDao<K, E> dao) {
        this.dao = dao;
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
