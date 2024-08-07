package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;

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
public class DaoOnlyBatchWriteService<K extends Key, E extends Entity<K>> extends AbstractBatchWriteService<E> {

    @Nonnull
    private BatchWriteDao<E> dao;

    @Nonnull
    private KeyGenerator<K> keyGenerator;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @param keyGenerator      主键生成器。
     * @since 1.5.6
     */
    public DaoOnlyBatchWriteService(
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel,
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyGenerator<K> keyGenerator
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyBatchWriteService(ServiceExceptionMapper, LogLevel, BatchWriteDao, KeyGenerator)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyBatchWriteService(ServiceExceptionMapper, LogLevel, BatchWriteDao, KeyGenerator)
     * @deprecated 使用 {@link #DaoOnlyBatchWriteService(ServiceExceptionMapper, LogLevel, BatchWriteDao, KeyGenerator)} 代替。
     */
    public DaoOnlyBatchWriteService(
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    @Deprecated
    public DaoOnlyBatchWriteService(
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
    }

    @Override
    protected void doWrite(E entity) throws Exception {
        if (Objects.isNull(entity.getKey())) {
            entity.setKey(keyGenerator.generate());
        }
        dao.write(entity);
    }

    @Override
    protected void doBatchWrite(List<E> entities) throws Exception {
        for (E entity : entities) {
            if (Objects.isNull(entity.getKey())) {
                entity.setKey(keyGenerator.generate());
            }
        }
        dao.batchWrite(entities);
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
