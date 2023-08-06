package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.SingleObjectCache;
import com.dwarfeng.subgrade.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.SingleObjectService;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * 通用的单对象服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class GeneralSingleObjectService<E extends Entity<?>> implements SingleObjectService<E> {

    @Nonnull
    private SingleObjectDao<E> dao;
    @Nonnull
    private SingleObjectCache<E> cache;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;
    @Nonnegative
    private long cacheTimeout;

    public GeneralSingleObjectService(
            @Nonnull SingleObjectDao<E> dao,
            @Nonnull SingleObjectCache<E> cache,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        this.dao = dao;
        this.cache = cache;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public boolean exists() throws ServiceException {
        try {
            return internalExists();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private boolean internalExists() throws Exception {
        if (cache.exists()) {
            return true;
        }
        return dao.exists();
    }

    @Override
    public E get() throws ServiceException {
        try {
            if (cache.exists()) {
                return cache.get();
            }
            E entity = dao.get();
            cache.put(entity, cacheTimeout);
            return entity;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void put(E entity) throws ServiceException {
        try {
            dao.put(entity);
            cache.put(entity, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void clear() throws ServiceException {
        try {
            dao.clear();
            cache.clear();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Nonnull
    public SingleObjectDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull SingleObjectDao<E> dao) {
        this.dao = dao;
    }

    @Nonnull
    public SingleObjectCache<E> getCache() {
        return cache;
    }

    public void setCache(@Nonnull SingleObjectCache<E> cache) {
        this.cache = cache;
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
        return "GeneralSingleObjectService{" +
                "dao=" + dao +
                ", cache=" + cache +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                ", cacheTimeout=" + cacheTimeout +
                '}';
    }
}
