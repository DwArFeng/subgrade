package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.ListCache;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;

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
public class GeneralEntireLookupService<E extends Entity<?>> extends AbstractEntireLookupService<E> {

    @Nonnull
    private EntireLookupDao<E> dao;

    @Nonnull
    private ListCache<E> cache;

    @Nonnegative
    private long cacheTimeout;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @param cache             列表缓存。
     * @param cacheTimeout      缓存超时时间。
     * @since 1.5.4
     */
    public GeneralEntireLookupService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull EntireLookupDao<E> dao,
            @Nonnull ListCache<E> cache,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.cacheTimeout = cacheTimeout;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #GeneralEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao, ListCache, long)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param cache             列表缓存。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param cacheTimeout      缓存超时时间。
     * @see #GeneralEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao, ListCache, long)
     * @deprecated 使用 {@link #GeneralEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao, ListCache, long)} 代替。
     */
    @Deprecated
    public GeneralEntireLookupService(
            @Nonnull EntireLookupDao<E> dao,
            @Nonnull ListCache<E> cache,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnegative long cacheTimeout
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.cache = cache;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    protected PagedData<E> doLookup() throws Exception {
        if (cache.exists()) {
            return PagingUtil.pagedData(cache.get());
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return PagingUtil.pagedData(lookup);
    }

    @Override
    protected PagedData<E> doLookup(PagingInfo pagingInfo) throws Exception {
        pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
        if (cache.exists()) {
            return PagingUtil.pagedData(pagingInfo, cache.size(), cache.get(pagingInfo));
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return PagingUtil.pagedData(pagingInfo, cache.size(), cache.get(pagingInfo));
    }

    @Override
    protected List<E> doLookupAsList() throws Exception {
        if (cache.exists()) {
            return cache.get();
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return lookup;
    }

    @Override
    protected List<E> doLookupAsList(PagingInfo pagingInfo) throws Exception {
        pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
        if (cache.exists()) {
            return cache.get(pagingInfo);
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return cache.get(pagingInfo);
    }

    @Override
    protected E doLookupFirst() throws Exception {
        if (cache.exists()) {
            return cache.get(PagingInfo.FIRST_ONE).stream().findFirst().orElse(null);
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return lookup.stream().findFirst().orElse(null);
    }

    @Override
    protected int doLookupCount() throws Exception {
        if (cache.exists()) {
            return cache.get().size();
        }
        List<E> lookup = dao.lookup();
        cache.set(lookup, cacheTimeout);
        return lookup.size();
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
                ", cacheTimeout=" + cacheTimeout +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
