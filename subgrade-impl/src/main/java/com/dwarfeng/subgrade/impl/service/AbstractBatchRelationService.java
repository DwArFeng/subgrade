package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchRelationService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 批量关系服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractBatchRelationService<PK extends Key, CK extends Key> implements
        BatchRelationService<PK, CK> {

    @Nonnull
    protected ServiceExceptionMapper sem;

    @Nonnull
    protected LogLevel exceptionLogLevel;

    public AbstractBatchRelationService(@Nonnull ServiceExceptionMapper sem, @Nonnull LogLevel exceptionLogLevel) {
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean existsRelation(PK pk, CK ck) throws ServiceException {
        try {
            return doExistsRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询关系是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询关系是否存在。
     *
     * @param pk 指定的父项对应的键。
     * @param ck 指定的子项对应的键。
     * @return 父项与子项是否存在关系。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #existsRelation(PK, CK)
     */
    protected abstract boolean doExistsRelation(PK pk, CK ck) throws Exception;

    @Override
    public void addRelation(PK pk, CK ck) throws ServiceException {
        try {
            doAddRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("添加关系时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 添加关系。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #addRelation(PK, CK)
     */
    protected abstract void doAddRelation(PK pk, CK ck) throws Exception;

    @Override
    public void deleteRelation(PK pk, CK ck) throws ServiceException {
        try {
            doDeleteRelation(pk, ck);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除关系时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 删除关系。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #deleteRelation(PK, CK)
     */
    protected abstract void doDeleteRelation(PK pk, CK ck) throws Exception;

    @Override
    public boolean existsAllRelations(PK pk, List<CK> cks) throws ServiceException {
        try {
            return doExistsAllRelations(pk, cks);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量查询关系是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量查询关系是否存在。
     *
     * <p>
     * 如果指定的父项与指定的子项全部存在关系，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部包含。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #existsAllRelations(PK, List)
     */
    protected abstract boolean doExistsAllRelations(PK pk, List<CK> cks) throws Exception;

    @Override
    public boolean existsNonRelations(PK pk, List<CK> cks) throws ServiceException {
        try {
            return doExistsNonRelations(pk, cks);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量查询关系是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量查询关系是否存在。
     *
     * <p>
     * 如果指定的父项与指定的子项全部不存在关系，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部不包含。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #existsNonRelations(PK, List)
     */
    protected abstract boolean doExistsNonRelations(PK pk, List<CK> cks) throws Exception;

    @Override
    public void batchAddRelations(PK pk, List<CK> cks) throws ServiceException {
        try {
            doBatchAddRelations(pk, cks);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量添加关系时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量添加关系。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchAddRelations(PK, List)
     */
    protected abstract void doBatchAddRelations(PK pk, List<CK> cks) throws Exception;

    @Override
    public void batchDeleteRelations(PK pk, List<CK> cks) throws ServiceException {
        try {
            doBatchDeleteRelations(pk, cks);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量删除关系时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量删除关系。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchDeleteRelations(PK, List)
     */
    protected abstract void doBatchDeleteRelations(PK pk, List<CK> cks) throws Exception;

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
        return "AbstractBatchRelationService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
