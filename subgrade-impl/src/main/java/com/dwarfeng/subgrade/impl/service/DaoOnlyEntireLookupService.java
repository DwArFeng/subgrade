package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 仅通过数据访问层实现的全体实体查询服务。
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
public class DaoOnlyEntireLookupService<E extends Entity<?>> implements EntireLookupService<E> {

    @Nonnull
    private EntireLookupDao<E> dao;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public DaoOnlyEntireLookupService(
            @Nonnull EntireLookupDao<E> dao,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public PagedData<E> lookup() throws ServiceException {
        try {
            return PagingUtil.pagedData(dao.lookup());
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询全部实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException {
        try {
            pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
            return PagingUtil.pagedData(pagingInfo, dao.lookupCount(), dao.lookup(pagingInfo));
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
            return dao.lookup();
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
            pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
            return dao.lookup(pagingInfo);
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
            return dao.lookupFirst();
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
            return dao.lookupCount();
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

    @Override
    public String toString() {
        return "DaoOnlyEntireLookupService{" +
                "dao=" + dao +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
