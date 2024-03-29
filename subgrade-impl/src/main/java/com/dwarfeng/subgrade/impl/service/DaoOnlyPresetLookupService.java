package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.PresetLookupService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 仅通过数据访问层实现的预设实体查询服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class DaoOnlyPresetLookupService<E extends Entity<?>> implements PresetLookupService<E> {

    @Nonnull
    private PresetLookupDao<E> dao;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public DaoOnlyPresetLookupService(
            @Nonnull PresetLookupDao<E> dao,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public PagedData<E> lookup(String preset, Object[] objs) throws ServiceException {
        try {
            return PagingUtil.pagedData(dao.lookup(preset, objs));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public PagedData<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws ServiceException {
        try {
            pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
            return PagingUtil.pagedData(
                    pagingInfo, dao.lookupCount(preset, objs), dao.lookup(preset, objs, pagingInfo)
            );
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.4
     */
    @Override
    public List<E> lookupAsList(String preset, Object[] objs) throws ServiceException {
        try {
            return dao.lookup(preset, objs);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.4
     */
    @Override
    public List<E> lookupAsList(String preset, Object[] objs, PagingInfo pagingInfo) throws ServiceException {
        try {
            pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
            return dao.lookup(preset, objs, pagingInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.2.8
     */
    @Override
    public E lookupFirst(String preset, Object[] objs) throws ServiceException {
        try {
            return dao.lookupFirst(preset, objs);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * @since 1.4.1
     */
    @Override
    public int lookupCount(String preset, Object[] objs) throws ServiceException {
        try {
            return dao.lookupCount(preset, objs);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询预设实体数量时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public PresetLookupDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull PresetLookupDao<E> dao) {
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
        return "DaoOnlyPresetLookupService{" +
                "dao=" + dao +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
