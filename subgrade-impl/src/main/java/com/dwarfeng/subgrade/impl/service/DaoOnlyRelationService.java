package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.RelationDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.RelationService;

import javax.annotation.Nonnull;

/**
 * 仅通过数据访问层实现的关系服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.2.3-beta
 */
public class DaoOnlyRelationService<PK extends Key, CK extends Key> implements RelationService<PK, CK> {

    @Nonnull
    private RelationDao<PK, CK> dao;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public DaoOnlyRelationService(
            @Nonnull RelationDao<PK, CK> dao,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean existsRelation(PK pk, CK ck) throws ServiceException {
        try {
            return dao.existsRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("查询实体关系是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void addRelation(PK pk, CK ck) throws ServiceException {
        try {
            dao.addRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("添加实体关系时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void deleteRelation(PK pk, CK ck) throws ServiceException {
        try {
            dao.deleteRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体关系时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Nonnull
    public RelationDao<PK, CK> getDao() {
        return dao;
    }

    public void setDao(@Nonnull RelationDao<PK, CK> dao) {
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
        return "DaoOnlyRelationService{" +
                "dao=" + dao +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
