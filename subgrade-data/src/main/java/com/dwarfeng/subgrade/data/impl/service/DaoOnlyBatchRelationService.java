package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.sdk.service.AbstractBatchRelationService;
import com.dwarfeng.subgrade.data.stack.dao.BatchRelationDao;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * 仅通过数据访问层实现的关系服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class DaoOnlyBatchRelationService<PK extends Key, CK extends Key> extends AbstractBatchRelationService<PK, CK> {

    @NotNull
    private BatchRelationDao<PK, CK> dao;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @since 1.5.4
     */
    public DaoOnlyBatchRelationService(
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel,
            @NotNull BatchRelationDao<PK, CK> dao
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyBatchRelationService(ServiceExceptionMapper, LogLevel, BatchRelationDao)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyBatchRelationService(ServiceExceptionMapper, LogLevel, BatchRelationDao)
     * @deprecated 使用 {@link #DaoOnlyBatchRelationService(ServiceExceptionMapper, LogLevel, BatchRelationDao)} 代替。
     */
    @Deprecated
    public DaoOnlyBatchRelationService(
            @NotNull BatchRelationDao<PK, CK> dao,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
    }

    @Override
    protected boolean doExistsRelation(PK pk, CK ck) throws Exception {
        return dao.existsRelation(pk, ck);
    }

    @Override
    protected void doAddRelation(PK pk, CK ck) throws Exception {
        dao.addRelation(pk, ck);
    }

    @Override
    protected void doDeleteRelation(PK pk, CK ck) throws Exception {
        dao.deleteRelation(pk, ck);
    }

    @Override
    protected boolean doExistsAllRelations(PK pk, List<CK> cks) throws Exception {
        return dao.existsAllRelations(pk, cks);
    }

    @Override
    protected boolean doExistsNonRelations(PK pk, List<CK> cks) throws Exception {
        return dao.existsNonRelations(pk, cks);
    }

    @Override
    protected void doBatchAddRelations(PK pk, List<CK> cks) throws Exception {
        dao.batchAddRelations(pk, cks);
    }

    @Override
    protected void doBatchDeleteRelations(PK pk, List<CK> cks) throws Exception {
        dao.batchDeleteRelations(pk, cks);
    }

    @NotNull
    public BatchRelationDao<PK, CK> getDao() {
        return dao;
    }

    public void setDao(@NotNull BatchRelationDao<PK, CK> dao) {
        this.dao = dao;
    }

    @Override
    public String toString() {
        return "DaoOnlyBatchRelationService{" +
                "dao=" + dao +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
