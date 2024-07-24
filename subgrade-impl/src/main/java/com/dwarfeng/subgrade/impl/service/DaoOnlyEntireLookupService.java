package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;

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
public class DaoOnlyEntireLookupService<E extends Entity<?>> extends AbstractEntireLookupService<E> {

    @Nonnull
    private EntireLookupDao<E> dao;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @since 1.5.4
     */
    public DaoOnlyEntireLookupService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull EntireLookupDao<E> dao
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao)
     * @deprecated 使用 {@link #DaoOnlyEntireLookupService(ServiceExceptionMapper, LogLevel, EntireLookupDao)} 代替。
     */
    @Deprecated
    public DaoOnlyEntireLookupService(
            @Nonnull EntireLookupDao<E> dao,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
    }

    @Override
    protected PagedData<E> doLookup() throws Exception {
        return PagingUtil.pagedData(dao.lookup());
    }

    @Override
    protected PagedData<E> doLookup(PagingInfo pagingInfo) throws Exception {
        pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
        return PagingUtil.pagedData(pagingInfo, dao.lookupCount(), dao.lookup(pagingInfo));
    }

    @Override
    protected List<E> doLookupAsList() throws Exception {
        return dao.lookup();
    }

    @Override
    protected List<E> doLookupAsList(PagingInfo pagingInfo) throws Exception {
        pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);
        return dao.lookup(pagingInfo);
    }

    @Override
    protected E doLookupFirst() throws Exception {
        return dao.lookupFirst();
    }

    @Override
    protected int doLookupCount() throws Exception {
        return dao.lookupCount();
    }

    @Nonnull
    public EntireLookupDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull EntireLookupDao<E> dao) {
        this.dao = dao;
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
