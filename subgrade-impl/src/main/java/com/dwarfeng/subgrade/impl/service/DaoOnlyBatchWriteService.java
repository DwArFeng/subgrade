package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchWriteService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * 仅通过数据访问层实现的批量写入服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class DaoOnlyBatchWriteService<K extends Key, E extends Entity<K>> implements BatchWriteService<E> {

    @Nonnull
    private BatchWriteDao<E> dao;
    @Nonnull
    private KeyGenerator<K> keyGenerator;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public DaoOnlyBatchWriteService(
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.keyGenerator = keyGenerator;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Deprecated
    public DaoOnlyBatchWriteService(
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.dao = dao;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public void write(E entity) throws ServiceException {
        try {
            if (Objects.isNull(entity.getKey())) {
                entity.setKey(keyGenerator.generate());
            }
            dao.write(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("写入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void batchWrite(List<E> entities) throws ServiceException {
        try {
            for (E entity : entities) {
                if (Objects.isNull(entity.getKey())) {
                    entity.setKey(keyGenerator.generate());
                }
            }
            dao.batchWrite(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("写入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public BatchWriteDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull BatchWriteDao<E> dao) {
        this.dao = dao;
    }

    @Nonnull
    public KeyGenerator<K> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(@Nonnull KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Deprecated
    @Nonnull
    public KeyFetcher<K> getKeyFetcher() {
        return KeyFetcherAdaptHelper.toKeyFetcher(keyGenerator);
    }

    @Deprecated
    public void setKeyFetcher(@Nonnull KeyFetcher<K> keyFetcher) {
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
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
        return "DaoOnlyBatchWriteService{" +
                "dao=" + dao +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
