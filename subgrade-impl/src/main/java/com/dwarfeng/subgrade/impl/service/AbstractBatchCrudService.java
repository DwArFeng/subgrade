package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchCrudService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 实体增删改查服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractBatchCrudService<K extends Key, E extends Entity<K>> implements BatchCrudService<K, E> {

    @Nonnull
    protected ServiceExceptionMapper sem;

    @Nonnull
    protected LogLevel exceptionLogLevel;

    public AbstractBatchCrudService(@Nonnull ServiceExceptionMapper sem, @Nonnull LogLevel exceptionLogLevel) {
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }


    @Override
    public boolean exists(K key) throws ServiceException {
        try {
            return doExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询实体是否存在。
     *
     * @param key 实体的键。
     * @return 实体是否存在。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #exists(Key)
     */
    protected abstract boolean doExists(K key) throws Exception;

    @Override
    public E get(K key) throws ServiceException {
        try {
            return doGet(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 获取实体。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #get(Key)
     */
    protected abstract E doGet(K key) throws Exception;

    @Override
    public K insert(E entity) throws ServiceException {
        try {
            return doInsert(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 插入实体。
     *
     * <p>
     * 当实体的主键不存在时，会尝试生成一个新的主键。
     *
     * <p>
     * 需要注意的是，插入行为可能会改变实体的主键，即使实体的主键已经被设置。
     *
     * <p>
     * 插入完成后，实体的主键会被设置为插入后的主键。
     *
     * @param entity 实体。
     * @return 实体对应的主键。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #insert(Entity)
     */
    protected abstract K doInsert(E entity) throws Exception;

    @Override
    public void update(E entity) throws ServiceException {
        try {
            doUpdate(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 更新实体。
     *
     * @param entity 实体。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #update(Entity)
     */
    protected abstract void doUpdate(E entity) throws Exception;

    @Override
    public void delete(K key) throws ServiceException {
        try {
            doDelete(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 删除实体。
     *
     * @param key 实体的键。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #delete(Key)
     */
    protected abstract void doDelete(K key) throws Exception;

    @Override
    public E getIfExists(K key) throws ServiceException {
        try {
            return doGetIfExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 获取实体。
     *
     * <p>
     * 如果存在指定的键，则获取实体；否则返回 <code>null</code>。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体，或者是 null。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #getIfExists(Key)
     */
    protected abstract E doGetIfExists(K key) throws Exception;

    @Override
    public K insertIfNotExists(E entity) throws ServiceException {
        try {
            return doInsertIfNotExists(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 插入实体。
     *
     * <p>
     * 如果不存在指定的实体，则插入实体，并返回插入后的主键；如果存在，则不做任何处理，并返回 <code>null</code>。
     *
     * <p>
     * 当实体不存在，需要插入实体时，需要注意以下事项：
     * <ul>
     *   <li>插入行为可能会改变实体的主键，即使实体的主键已经被设置。</li>
     *   <li>插入完成后，实体的主键会被设置为插入后的主键。</li>
     * </ul>
     *
     * @param entity 实体。
     * @return 实体插入后的主键，或者是 null。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #insertIfNotExists(Entity)
     */
    protected abstract K doInsertIfNotExists(E entity) throws Exception;

    @Override
    public void updateIfExists(E entity) throws ServiceException {
        try {
            doUpdateIfExists(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 更新实体。
     *
     * <p>
     * 如果存在指定的实体，则更新实体；否则不做任何处理。
     *
     * @param entity 实体。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #updateIfExists(Entity)
     */
    protected abstract void doUpdateIfExists(E entity) throws Exception;

    @Override
    public void deleteIfExists(K key) throws ServiceException {
        try {
            doDeleteIfExists(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 删除实体。
     *
     * <p>
     * 如果存在指定的实体，则删除实体；否则不做任何处理。
     *
     * @param key 实体的键。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #deleteIfExists(Key)
     */
    protected abstract void doDeleteIfExists(K key) throws Exception;

    @Override
    public K insertOrUpdate(E entity) throws ServiceException {
        try {
            return doInsertOrUpdate(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 插入或更新实体。
     *
     * <p>
     * 如果实体不存在，则插入实体，并返回插入后的主键；如果实体存在，则更新实体，并返回 <code>null</code>。
     *
     * <p>
     * 当实体不存在，需要插入实体时，需要注意以下事项：
     * <ul>
     *   <li>插入行为可能会改变实体的主键，即使实体的主键已经被设置。</li>
     *   <li>插入完成后，实体的主键会被设置为插入后的主键。</li>
     * </ul>
     *
     * @param entity 实体。
     * @return 如果实体被插入，返回插入后的主键；如果实体被更新，返回 null。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #insertOrUpdate(Entity)
     */
    protected abstract K doInsertOrUpdate(E entity) throws Exception;

    @Override
    public boolean allExists(List<K> keys) throws ServiceException {
        try {
            return doAllExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量查询实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量查询实体是否存在。
     *
     * <p>
     * 如果指定的实体键列表中的所有键对应的实体全部存在，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param keys 实体的键组成的列表。
     * @return 指定的主键是否全部存在。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #allExists(List)
     */
    protected abstract boolean doAllExists(List<K> keys) throws Exception;

    @Override
    public boolean nonExists(List<K> keys) throws ServiceException {
        try {
            return doNonExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量查询实体是否存在时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量查询实体是否存在。
     *
     * <p>
     * 如果指定的实体键列表中的所有键对应的实体全部不存在，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param keys 实体的键组成的列表。
     * @return 指定的主键是否全部不存在。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #nonExists(List)
     */
    protected abstract boolean doNonExists(List<K> keys) throws Exception;

    @Override
    public List<E> batchGet(List<K> keys) throws ServiceException {
        try {
            return doBatchGet(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量获取实体。
     *
     * @param keys 实体的键组成的列表。
     * @return 实体的键对应的实体组成的列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchGet(List)
     */
    protected abstract List<E> doBatchGet(List<K> keys) throws Exception;

    @Override
    public List<K> batchInsert(List<E> entities) throws ServiceException {
        try {
            return doBatchInsert(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量插入实体。
     *
     * <p>
     * 对于批量操作的每一个实体，当实体的主键不存在时，会尝试生成一个新的主键。
     *
     * <p>
     * 对于批量操作的每一个实体，需要注意的是，插入行为可能会改变实体的主键，即使实体的主键已经被设置。
     *
     * <p>
     * 批量操作的每一个实体，插入完成后，对于实体的主键会被设置为插入后的主键。
     *
     * @param entities 实体组成的列表
     * @return 插入的实体对应的主键。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchInsert(List)
     */
    protected abstract List<K> doBatchInsert(List<E> entities) throws Exception;

    @Override
    public void batchUpdate(List<E> entities) throws ServiceException {
        try {
            doBatchUpdate(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量更新实体。
     *
     * @param entities 实体组成的列表
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchUpdate(List)
     */
    protected abstract void doBatchUpdate(List<E> entities) throws Exception;

    @Override
    public void batchDelete(List<K> keys) throws ServiceException {
        try {
            doBatchDelete(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量删除实体。
     *
     * @param keys 实体的键组成的列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchDelete(List)
     */
    protected abstract void doBatchDelete(List<K> keys) throws Exception;

    @Override
    public List<E> batchGetIfExists(List<K> keys) throws ServiceException {
        try {
            return doBatchGetIfExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量获取实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量获取实体。
     *
     * <p>
     * 对于批量操作中的每一个键，如果对应的实体存在，则获取该实体；否则不做任何处理。<br>
     * 将所有获取的实体组成一个列表返回。
     *
     * <p>
     * 返回的列表中的实体的个数小于等于键列表的个数。
     *
     * @param keys 实体的键组成的列表。
     * @return 指定且存在的键对应的实体组成的列表，实体的个数小于等于键列表的个数。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchGetIfExists(List)
     */
    protected abstract List<E> doBatchGetIfExists(List<K> keys) throws Exception;

    @Deprecated
    @Override
    public List<K> batchInsertIfExists(List<E> entities) throws ServiceException {
        try {
            return doBatchInsertIfNotExists(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    @Override
    public List<K> batchInsertIfNotExists(List<E> entities) throws ServiceException {
        try {
            return doBatchInsertIfNotExists(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量插入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量插入实体。
     *
     * <p>
     * 对于批量操作的每一个实体，如果实体不存在，则插入，并获取插入后的主键；否则不做任何处理。<br>
     * 将所有插入的实体的主键组成一个列表返回。
     *
     * <p>
     * 对于批量操作的每一个实体，当实体不存在，需要插入实体时，需要注意以下事项：
     * <ul>
     *   <li>插入行为可能会改变实体的主键，即使实体的主键已经被设置。</li>
     *   <li>插入完成后，实体的主键会被设置为插入后的主键。</li>
     * </ul>
     *
     * <p>
     * 返回的列表中的主键的个数小于等于实体列表的个数。
     *
     * @param entities 实体组成的列表
     * @return 插入的实体对应的主键。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchInsertIfNotExists(List)
     */
    protected abstract List<K> doBatchInsertIfNotExists(List<E> entities) throws Exception;

    @Override
    public void batchUpdateIfExists(List<E> entities) throws ServiceException {
        try {
            doBatchUpdateIfExists(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量更新实体。
     *
     * <p>
     * 对于批量操作的每一个实体，如果实体存在，则更新；否则不做任何处理。
     *
     * @param entities 实体组成的列表
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchUpdateIfExists(List)
     */
    protected abstract void doBatchUpdateIfExists(List<E> entities) throws Exception;

    @Override
    public void batchDeleteIfExists(List<K> keys) throws ServiceException {
        try {
            doBatchDeleteIfExists(keys);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量删除实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量删除实体。
     *
     * <p>
     * 对于批量操作的每一个键，如果对应的实体存在，则删除；否则不做任何处理。
     *
     * @param keys 实体的键组成的列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchDeleteIfExists(List)
     */
    protected abstract void doBatchDeleteIfExists(List<K> keys) throws Exception;

    @Override
    public List<K> batchInsertOrUpdate(List<E> entities) throws ServiceException {
        try {
            return doBatchInsertOrUpdate(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量插入或更新实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 批量插入或更新实体。
     *
     * <p>
     * 对于批量操作的每一个实体，如果实体不存在，则插入实体，并获取插入后的主键；否则更新实体。<br>
     * 将所有插入或更新的实体的主键组成一个列表返回。
     *
     * <p>
     * 对于批量操作的每一个实体，当实体不存在，需要插入实体时，需要注意以下事项：
     * <ul>
     *   <li>插入行为可能会改变实体的主键，即使实体的主键已经被设置。</li>
     *   <li>插入完成后，实体的主键会被设置为插入后的主键。</li>
     * </ul>
     *
     * <p>
     * 返回的列表中的主键的个数小于等于实体列表的个数。
     *
     * @param entities 实体组成的列表
     * @return 所有被插入实体的主键，小于或等于 entities 的个数。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #batchInsertOrUpdate(List)
     */
    protected abstract List<K> doBatchInsertOrUpdate(List<E> entities) throws Exception;

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
        return "AbstractBatchCrudService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
