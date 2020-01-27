package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.sdk.util.PagingUtil;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.PresetLookupService;
import org.springframework.lang.NonNull;

/**
 * 仅用数据访问层实现的预设实体查询服务。
 * <p>该类同时使用数据访问层和缓存实现实体的查询方法。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class DaoOnlyPresetLookupService<E extends Entity<?>> implements PresetLookupService<E> {

    private PresetLookupDao<E> dao;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;
    private long cacheTimeout;

    public DaoOnlyPresetLookupService(
            @NonNull PresetLookupDao<E> dao,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel,
            @NonNull long cacheTimeout) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
        this.cacheTimeout = cacheTimeout;
    }

    @Override
    public PagedData<E> lookup(String preset, Object[] objs) throws ServiceException {
        try {
            return PagingUtil.pagedData(dao.lookup(preset, objs));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public PagedData<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws ServiceException {
        try {
            return PagingUtil.pagedData(pagingInfo, dao.lookupCount(preset, objs), dao.lookup(preset, objs, pagingInfo));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public PresetLookupDao<E> getDao() {
        return dao;
    }

    public void setDao(@NonNull PresetLookupDao<E> dao) {
        this.dao = dao;
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
