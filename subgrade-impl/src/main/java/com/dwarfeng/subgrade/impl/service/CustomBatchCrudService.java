package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.sdk.service.custom.operation.BatchCrudOperation;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchCrudService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 自定义的批量实体增删改查服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务或同步锁，请通过代理的方式在代理类中添加事务或者同步锁。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class CustomBatchCrudService<K extends Key, E extends Entity<K>> implements BatchCrudService<K, E> {

    @Nonnull
    private BatchCrudOperation<K, E> operation;
    @Nonnull
    private KeyGenerator<K> keyGenerator;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public CustomBatchCrudService(
            @Nonnull BatchCrudOperation<K, E> operation,
            @Nonnull KeyGenerator<K> keyGenerator,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.operation = operation;
        this.keyGenerator = keyGenerator;
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Deprecated
    public CustomBatchCrudService(
            @Nonnull BatchCrudOperation<K, E> operation,
            @Nonnull KeyFetcher<K> keyFetcher,
            @Nonnull ServiceExceptionMapper sem,
            @Nonnull LogLevel exceptionLogLevel
    ) {
        this.operation = operation;
        this.keyGenerator = KeyFetcherAdaptHelper.toKeyGenerator(keyFetcher);
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("获取实体信息时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private K internalInsert(E element) throws Exception {
        if (Objects.isNull(element.getKey())) {
            element.setKey(keyGenerator.generate());
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
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void updateIfExists(E element) throws ServiceException {
        try {
            if (internalExists(element.getKey())) {
                internalUpdate(element);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void deleteIfExists(K key) throws ServiceException {
        try {
            if (internalExists(key)) {
                internalDelete(key);
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
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
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public boolean allExists(List<K> keys) throws ServiceException {
        try {
            return internalAllExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalAllExists(List<K> keys) throws Exception {
        return operation.allExists(keys);
    }

    @Override
    public boolean nonExists(List<K> keys) throws ServiceException {
        try {
            return internalNonExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalNonExists(List<K> keys) throws Exception {
        return operation.nonExists(keys);
    }

    @Override
    public List<E> batchGet(List<K> keys) throws ServiceException {
        try {
            return internalBatchGet(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private List<E> internalBatchGet(List<K> keys) throws Exception {
        if (!operation.allExists(keys)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        return operation.batchGet(keys);
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws ServiceException {
        try {
            return internalBatchInsert(elements);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private List<K> internalBatchInsert(List<E> elements) throws Exception {
        List<K> collect = elements.stream().filter(e -> Objects.nonNull(e.getKey())).map(E::getKey).collect(Collectors.toList());
        if (!internalNonExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }

        List<E> nonKeyElements = elements.stream().filter(e -> Objects.isNull(e.getKey())).collect(Collectors.toList());
        List<K> generatedKeys = keyGenerator.batchGenerate(nonKeyElements.size());
        for (int i = 0; i < nonKeyElements.size(); i++) {
            nonKeyElements.get(i).setKey(generatedKeys.get(i));
        }

        return operation.batchInsert(elements);
    }

    @Override
    public void batchUpdate(List<E> elements) throws ServiceException {
        try {
            internalBatchUpdate(elements);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalBatchUpdate(List<E> elements) throws Exception {
        List<K> collect = elements.stream().map(E::getKey).collect(Collectors.toList());
        if (!internalAllExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        operation.batchUpdate(elements);
    }

    @Override
    public void batchDelete(List<K> keys) throws ServiceException {
        try {
            internalBatchDelete(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalBatchDelete(List<K> keys) throws Exception {
        if (!internalAllExists(keys)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        operation.batchDelete(keys);
    }

    @Override
    public List<E> batchGetIfExists(List<K> keys) throws ServiceException {
        try {
            List<K> existsKeys = new ArrayList<>();
            for (K key : keys) {
                if (internalExists(key)) {
                    existsKeys.add(key);
                }
            }
            return internalBatchGet(existsKeys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public List<K> batchInsertIfExists(List<E> elements) throws ServiceException {
        try {
            List<E> elements2Insert = new ArrayList<>();
            for (E element : elements) {
                if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                    elements2Insert.add(element);
                }
            }
            return internalBatchInsert(elements2Insert);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void batchUpdateIfExists(List<E> elements) throws ServiceException {
        try {
            List<E> elements2Update = new ArrayList<>();
            for (E element : elements) {
                if (internalExists(element.getKey())) {
                    elements2Update.add(element);
                }
            }
            internalBatchUpdate(elements2Update);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void batchDeleteIfExists(List<K> keys) throws ServiceException {
        try {
            List<K> keys2Delete = new ArrayList<>();
            for (K key : keys) {
                if (internalExists(key)) {
                    keys2Delete.add(key);
                }
            }
            internalBatchDelete(keys2Delete);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public List<K> batchInsertOrUpdate(List<E> elements) throws ServiceException {
        try {
            List<E> elements2Insert = new ArrayList<>();
            List<E> elements2Update = new ArrayList<>();
            for (E element : elements) {
                if (Objects.isNull(element.getKey()) || !internalExists(element.getKey())) {
                    elements2Insert.add(element);
                } else {
                    elements2Update.add(element);
                }
            }
            internalBatchUpdate(elements2Update);
            return internalBatchInsert(elements2Insert);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public BatchCrudOperation<K, E> getOperation() {
        return operation;
    }

    public void setOperation(@Nonnull BatchCrudOperation<K, E> operation) {
        this.operation = operation;
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
        return "CustomBatchCrudService{" +
                "operation=" + operation +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
