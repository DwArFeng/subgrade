package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.data.stack.service.SingleObjectService;

import org.jetbrains.annotations.NotNull;

/**
 * 仅通过数据访问层实现的单对象服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class DaoOnlySingleObjectService<E extends Entity<?>> implements SingleObjectService<E> {

    @NotNull
    private SingleObjectDao<E> dao;
    @NotNull
    private ServiceExceptionMapper sem;
    @NotNull
    private LogLevel exceptionLogLevel;

    public DaoOnlySingleObjectService(
            @NotNull SingleObjectDao<E> dao,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean exists() throws ServiceException {
        try {
            return dao.exists();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public E get() throws ServiceException {
        try {
            return dao.get();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void put(E entity) throws ServiceException {
        try {
            dao.put(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void clear() throws ServiceException {
        try {
            dao.clear();
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

    @Override
    public String toString() {
        return "DaoOnlySingleObjectService{" +
                "dao=" + dao +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
