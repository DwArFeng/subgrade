package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.ListCache;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * 通用的全体实体查询服务。
 *
 * <p>
 * 该类同时使用数据访问层和缓存实现实体的查询方法。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class GeneralEntireLookupService<E extends Entity<?>> implements EntireLookupService<E> {

    @Nonnull
    private EntireLookupDao<E> dao;
    @Nonnull
    private ListCache<E> cache;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;
    @Nonnegative
    private long cacheTimeout;

    public GeneralEntireLookupService(
            @Nonnull EntireLookupDao<E> dao,
            @Nonnull ListCache<E> cache,
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
    public PagedData<E> lookup() throws ServiceException {
        try {
            if (cache.exists()) {
                return PagingUtil.pagedData(cache.get());
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return PagingUtil.pagedData(lookup);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException {
        try {
            if (cache.exists()) {
                return PagingUtil.pagedData(pagingInfo, cache.size(), cache.get(pagingInfo));
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return PagingUtil.pagedData(pagingInfo, cache.size(), cache.get(pagingInfo));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.4
     */
    @Override
    public List<E> lookupAsList() throws ServiceException {
        try {
            if (cache.exists()) {
                return cache.get();
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return lookup;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.4
     */
    @Override
    public List<E> lookupAsList(PagingInfo pagingInfo) throws ServiceException {
        try {
            if (cache.exists()) {
                return cache.get(pagingInfo);
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return cache.get(pagingInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.8
     */
    @Override
    public E lookupFirst() throws ServiceException {
        try {
            if (cache.exists()) {
                return cache.get(PagingInfo.FIRST_ONE).stream().findFirst().orElse(null);
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return lookup.stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.4.1
     */
    @Override
    public int lookupCount() throws ServiceException {
        try {
            if (cache.exists()) {
                return cache.get().size();
            }
            List<E> lookup = dao.lookup();
            cache.set(lookup, cacheTimeout);
            return lookup.size();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体数量时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public EntireLookupDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull EntireLookupDao<E> dao) {
        this.dao = dao;
    }

    @Nonnull
    public ListCache<E> getCache() {
        return cache;
    }

    public void setCache(@Nonnull ListCache<E> cache) {
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
        return "GeneralEntireLookupService{" +
                "dao=" + dao +
                ", cache=" + cache +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                ", cacheTimeout=" + cacheTimeout +
                '}';
    }
}
