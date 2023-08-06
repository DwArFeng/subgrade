package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.sdk.service.custom.operation.CrudOperation;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.CrudService;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 自定义的实体增删改查服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务或同步锁，请通过代理的方式在代理类中添加事务或者同步锁。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class CustomCrudService<K extends Key, E extends Entity<K>> implements CrudService<K, E> {

    @Nonnull
    private CrudOperation<K, E> operation;
    @Nonnull
    private KeyFetcher<K> keyFetcher;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public CustomCrudService(
            @Nonnull CrudOperation<K, E> operation,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.operation = operation;
        this.keyFetcher = keyFetcher;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private boolean internalExists(K key) throws Exception {
        return operation.exists(key);
    }

    @Override
    public E get(K key) throws ServiceException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体信息时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private E internalGet(K key) throws Exception {
        if (!operation.exists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        return operation.get(key);
    }

    @Override
    public K insert(E element) throws ServiceException {
        try {
            return internalInsert(element);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private K internalInsert(E element) throws Exception {
        if (Objects.isNull(element.getKey())) {
            element.setKey(keyFetcher.fetchKey());
        }
        if (internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }
        return operation.insert(element);
    }

    @Override
    public void update(E element) throws ServiceException {
        try {
            internalUpdate(element);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private void internalUpdate(E element) throws Exception {
        if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        operation.update(element);
    }

    @Override
    public void delete(K key) throws ServiceException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private void internalDelete(K key) throws Exception {
        if (Objects.isNull(key) || !internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        operation.delete(key);
    }

    @Override
    public E getIfExists(K key) throws ServiceException {
        try {
            return internalExists(key) ? internalGet(key) : null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insertIfNotExists(E element) throws ServiceException {
        try {
            if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                return internalInsert(element);
            }
            return null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void updateIfExists(E element) throws ServiceException {
        try {
            if (internalExists(element.getKey())) {
                internalUpdate(element);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public void deleteIfExists(K key) throws ServiceException {
        try {
            if (internalExists(key)) {
                internalDelete(key);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Override
    public K insertOrUpdate(E element) throws ServiceException {
        try {
            if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                return internalInsert(element);
            } else {
                internalUpdate(element);
                return null;
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入或更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    @Nonnull
    public CrudOperation<K, E> getOperation() {
        return operation;
    }

    public void setOperation(@Nonnull CrudOperation<K, E> operation) {
        this.operation = operation;
    }

    @Nonnull
    public KeyFetcher<K> getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(@Nonnull KeyFetcher<K> keyFetcher) {
        this.keyFetcher = keyFetcher;
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
        return "CustomCrudService{" +
                "operation=" + operation +
                ", keyFetcher=" + keyFetcher +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
