package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.stack.cache.SingleObjectCache;
import com.dwarfeng.subgrade.data.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.data.stack.service.SingleObjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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

    @NotNull
    private SingleObjectDao<E> dao;
    @NotNull
    private SingleObjectCache<E> cache;
    @NotNull
    private ServiceExceptionMapper sem;
    @NotNull
    private LogLevel exceptionLogLevel;
    @Range(from = 0, to = Long.MAX_VALUE)
    private long cacheTimeout;

    public GeneralSingleObjectService(
            @NotNull SingleObjectDao<E> dao,
            @NotNull SingleObjectCache<E> cache,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel,
            @Range(from = 0, to = Long.MAX_VALUE) long cacheTimeout
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
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void put(E entity) throws ServiceException {
        try {
            dao.put(entity);
            cache.put(entity, cacheTimeout);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void clear() throws ServiceException {
        try {
            dao.clear();
            cache.clear();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @NotNull
    public SingleObjectDao<E> getDao() {
        return dao;
    }

    public void setDao(@NotNull SingleObjectDao<E> dao) {
        this.dao = dao;
    }

    @NotNull
    public SingleObjectCache<E> getCache() {
        return cache;
    }

    public void setCache(@NotNull SingleObjectCache<E> cache) {
        this.cache = cache;
    }

    @NotNull
    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@NotNull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    @NotNull
    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@NotNull LogLevel exceptionLogLevel) {
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
