package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchCrudService;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 仅通过数据访问层的实体增删改查服务。
 * <p>该类同时使用数据访问层和缓存实现实体的增删改查方法。</p>
 * <p>插入没有主键的对象时，该服务会试图通过主键抓取器抓取新的主键，如果没有主键抓取器，就会报出异常。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DaoOnlyBatchCrudService<K extends Key, E extends Entity<K>> implements BatchCrudService<K, E> {

    private BatchBaseDao<K, E> dao;
    private KeyFetcher<K> keyFetcher;
    private ServiceExceptionMapper sem;
    private LogLevel exceptionLogLevel;

    public DaoOnlyBatchCrudService(
            @NonNull BatchBaseDao<K, E> dao,
            @NonNull KeyFetcher<K> keyFetcher,
            @NonNull ServiceExceptionMapper sem,
            @NonNull LogLevel exceptionLogLevel) {
        this.dao = dao;
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

    @Override
    public E get(K key) throws ServiceException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("获取实体信息时发生异常", exceptionLogLevel, sem, e);
        }
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
        } else if (internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }
        return dao.insert(element);
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
        if (!internalExists(element.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.update(element);
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
        if (!internalExists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.delete(key);
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

    @SuppressWarnings("DuplicatedCode")
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

    private boolean internalExists(K key) throws Exception {
        return dao.exists(key);
    }

    private E internalGet(K key) throws Exception {
        return dao.get(key);
    }

    @Override
    public boolean allExists(List<K> keys) throws ServiceException {
        try {
            return internalAllExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private boolean internalAllExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (!internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public boolean nonExists(List<K> keys) throws ServiceException {
        try {
            return internalNonExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private boolean internalNonExists(List<K> keys) throws Exception {
        for (K key : keys) {
            if (internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public List<E> batchGet(List<K> keys) throws ServiceException {
        try {
            return internalBatchGet(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("判断实体是否存在时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private List<E> internalBatchGet(List<K> keys) throws Exception {
        List<E> elements = new ArrayList<>();
        for (K key : keys) {
            elements.add(internalGet(key));
        }
        return elements;
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws ServiceException {
        try {
            return internalBatchInsert(elements);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private List<K> internalBatchInsert(List<E> elements) throws Exception {
        List<K> collect = elements.stream().filter(e -> Objects.nonNull(e.getKey())).map(E::getKey).collect(Collectors.toList());
        if (!internalNonExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }

        for (E element : elements) {
            if (Objects.isNull(element.getKey())) {
                element.setKey(keyFetcher.fetchKey());
            }
        }

        return dao.batchInsert(elements);
    }

    @Override
    public void batchUpdate(List<E> elements) throws ServiceException {
        try {
            internalBatchUpdate(elements);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private void internalBatchUpdate(List<E> elements) throws Exception {
        List<K> collect = elements.stream().map(E::getKey).collect(Collectors.toList());
        if (!internalAllExists(collect)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.batchUpdate(elements);
    }

    @Override
    public void batchDelete(List<K> keys) throws ServiceException {
        try {
            internalBatchDelete(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    private void internalBatchDelete(List<K> keys) throws Exception {
        if (!internalAllExists(keys)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.batchDelete(keys);
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
            throw ServiceExceptionHelper.logAndThrow("获取实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("插入实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("更新实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("删除实体时发生异常", exceptionLogLevel, sem, e);
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
            throw ServiceExceptionHelper.logAndThrow("插入或更新实体时发生异常", exceptionLogLevel, sem, e);
        }
    }

    public BatchBaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@NonNull BatchBaseDao<K, E> dao) {
        this.dao = dao;
    }

    public KeyFetcher<K> getKeyFetcher() {
        return keyFetcher;
    }

    public void setKeyFetcher(@NonNull KeyFetcher<K> keyFetcher) {
        this.keyFetcher = keyFetcher;
    }

    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@NonNull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@NonNull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
    }
}
