package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.RelationDao;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;

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
public class DaoOnlyRelationService<PK extends Key, CK extends Key> extends AbstractRelationService<PK, CK> {

    @Nonnull
    private RelationDao<PK, CK> dao;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @since 1.5.4
     */
    public DaoOnlyRelationService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull RelationDao<PK, CK> dao
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyRelationService(ServiceExceptionMapper, LogLevel, RelationDao)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyRelationService(ServiceExceptionMapper, LogLevel, RelationDao)
     * @deprecated 使用 {@link #DaoOnlyRelationService(ServiceExceptionMapper, LogLevel, RelationDao)} 代替。
     */
    @Deprecated
    public DaoOnlyRelationService(
            @Nonnull RelationDao<PK, CK> dao,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
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

    @Nonnull
    public RelationDao<PK, CK> getDao() {
        return dao;
    }

    public void setDao(@Nonnull RelationDao<PK, CK> dao) {
        this.dao = dao;
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
