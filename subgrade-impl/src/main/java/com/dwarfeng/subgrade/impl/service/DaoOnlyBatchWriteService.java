package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchWriteService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * 仅通过数据访问层实现的批量写入服务。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class DaoOnlyBatchWriteService<K extends Key, E extends Entity<K>> implements BatchWriteService<E> {

    private BatchWriteDao<E> dao;
    private KeyFetcher<K> keyFetcher;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;

    public DaoOnlyBatchWriteService(
            @Nonnull BatchWriteDao<E> dao,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel) {
        this.dao = dao;
        this.keyFetcher = keyFetcher;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public void write(E element) throws ServiceException {
        try {
            if (Objects.isNull(element.getKey())) {
                element.setKey(keyFetcher.fetchKey());
            }
            dao.write(element);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("写入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void batchWrite(List<E> elements) throws ServiceException {
        try {
            for (E element : elements) {
                if (Objects.isNull(element.getKey())) {
                    element.setKey(keyFetcher.fetchKey());
                }
            }
            dao.batchWrite(elements);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("写入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public BatchWriteDao<E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull BatchWriteDao<E> dao) {
        this.dao = dao;
    }

    public KeyFetcher<K> getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(@Nonnull KeyFetcher<K> keyFetcher) {
        this.keyFetcher = keyFetcher;
    }

    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@Nonnull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@Nonnull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
    }
}
