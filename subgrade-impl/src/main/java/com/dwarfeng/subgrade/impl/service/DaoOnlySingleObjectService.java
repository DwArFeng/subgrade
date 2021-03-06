package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.SingleObjectService;
import org.springframework.lang.NonNull;

/**
 * 仅通过数据访问层实现的单对象服务。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class DaoOnlySingleObjectService<E extends Entity<?>> implements SingleObjectService<E> {

    private SingleObjectDao<E> dao;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;

    public DaoOnlySingleObjectService(
            @NonNull SingleObjectDao<E> dao,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean exists() throws ServiceException {
        try {
            return dao.exists();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public E get() throws ServiceException {
        try {
            return dao.get();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void put(E entity) throws ServiceException {
        try {
            dao.put(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void clear() throws ServiceException {
        try {
            dao.clear();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public SingleObjectDao<E> getDao() {
        return dao;
    }

    public void setDao(@NonNull SingleObjectDao<E> dao) {
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
