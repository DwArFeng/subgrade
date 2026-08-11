package com.dwarfeng.subgrade.data.sdk.service;

import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.stack.service.RelationService;

import org.jetbrains.annotations.NotNull;

/**
 * 关系服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractRelationService<PK extends Key, CK extends Key> implements RelationService<PK, CK> {

    @NotNull
    protected ServiceExceptionMapper sem;

    @NotNull
    protected LogLevel exceptionLogLevel;

    public AbstractRelationService(@NotNull ServiceExceptionMapper sem, @NotNull LogLevel exceptionLogLevel) {
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
        return "AbstractRelationService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
