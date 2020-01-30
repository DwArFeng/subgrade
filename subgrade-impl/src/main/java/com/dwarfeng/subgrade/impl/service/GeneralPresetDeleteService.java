package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.dao.PresetDeleteDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.PresetDeleteService;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 仅用数据访问层实现的预设实体删除服务。
 * <p>该类同时使用数据访问层和缓存实现实体的查询方法。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class GeneralPresetDeleteService<K extends Key, E extends Entity<K>> implements PresetDeleteService<E> {

    private PresetDeleteDao<K, E> dao;
    private BatchBaseCache<K, E> cache;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;

    public GeneralPresetDeleteService(
            @NonNull PresetDeleteDao<K, E> dao,
            @NonNull BatchBaseCache<K, E> cache,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel) {
        this.dao = dao;
        this.cache = cache;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
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

    @Override
    public void lookupDelete(String preset, Object[] objs) throws ServiceException {
        try {
            List<K> ks = dao.lookupDelete(preset, objs);
            cache.batchDelete(ks);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public PresetDeleteDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NonNull PresetDeleteDao<K, E> dao) {
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
}
