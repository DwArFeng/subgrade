package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.OptionalLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.OptionalLookupService;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * 仅通过数据访问层实现的可选实体查询服务。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class DaoOnlyOptionalLookupService<E extends Entity<?>> implements OptionalLookupService<E> {

    private OptionalLookupDao<E> dao;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;

    public DaoOnlyOptionalLookupService(
            @NonNull OptionalLookupDao<E> dao,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public PagedData<E> lookup(Map<String, Object[]> optional) throws ServiceException {
        try {
            return PagingUtil.pagedData(dao.lookup(optional));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public PagedData<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional) throws ServiceException {
        try {
            return PagingUtil.pagedData(dao.lookup(andOptional, orOptional, notOptional, orderOptional));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public PagedData<E> lookup(Map<String, Object[]> optional, PagingInfo pagingInfo) throws ServiceException {
        try {
            return PagingUtil.pagedData(pagingInfo, dao.lookupCount(optional), dao.lookup(optional, pagingInfo));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public PagedData<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional,
            PagingInfo pagingInfo) throws ServiceException {
        try {
            return PagingUtil.pagedData(
                    pagingInfo,
                    dao.lookupCount(andOptional, orOptional, notOptional),
                    dao.lookup(andOptional, orOptional, notOptional, orderOptional, pagingInfo));
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询预设实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public OptionalLookupDao<E> getDao() {
        return dao;
    }

    public void setDao(@NonNull OptionalLookupDao<E> dao) {
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
