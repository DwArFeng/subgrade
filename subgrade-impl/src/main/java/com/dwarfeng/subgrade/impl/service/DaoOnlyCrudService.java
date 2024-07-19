package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.CrudService;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 仅通过数据访问层实现的实体增删改查服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class DaoOnlyCrudService<K extends Key, E extends Entity<K>> implements CrudService<K, E> {

    @Nonnull
    private BaseDao<K, E> dao;
    @Nonnull
    private KeyGenerator<K> keyGenerator;
    @Nonnull
    private ServiceExceptionMapper sem;
    @Nonnull
    private LogLevel exceptionLogLevel;

    public DaoOnlyCrudService(
            @Nonnull BaseDao<K, E> dao,
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
    public DaoOnlyCrudService(
            @Nonnull BaseDao<K, E> dao,
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
    public boolean exists(K key) throws ServiceException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("判断实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private boolean internalExists(K key) throws Exception {
        return dao.exists(key);
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
        if (!dao.exists(key)) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }
        return dao.get(key);
    }

    @Override
    public K insert(E entity) throws ServiceException {
        try {
            return internalInsert(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private K internalInsert(E entity) throws Exception {
        if (Objects.isNull(entity.getKey())) {
            entity.setKey(keyGenerator.generate());
        } else if (internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_EXISTED);
        }
        return dao.insert(entity);
    }

    @Override
    public void update(E entity) throws ServiceException {
        try {
            internalUpdate(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    private void internalUpdate(E entity) throws Exception {
        if (!internalExists(entity.getKey())) {
            throw new ServiceException(ServiceExceptionCodes.ENTITY_NOT_EXIST);
        }

        dao.update(entity);
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
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public K insertIfNotExists(E entity) throws ServiceException {
        try {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                return internalInsert(entity);
            }
            return null;
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public void updateIfExists(E entity) throws ServiceException {
        try {
            if (internalExists(entity.getKey())) {
                internalUpdate(entity);
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
    public K insertOrUpdate(E entity) throws ServiceException {
        try {
            if (Objects.isNull(entity.getKey()) || !internalExists(entity.getKey())) {
                return internalInsert(entity);
            } else {
                internalUpdate(entity);
                return null;
            }
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Nonnull
    public BaseDao<K, E> getDao() {
        return dao;
    }

    public void setDao(@Nonnull BaseDao<K, E> dao) {
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
        return "DaoOnlyCrudService{" +
                "dao=" + dao +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
