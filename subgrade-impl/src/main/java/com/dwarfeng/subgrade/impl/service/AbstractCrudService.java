package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.CrudService;

import javax.annotation.Nonnull;

/**
 * 实体增删改查服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractCrudService<K extends Key, E extends Entity<K>> implements CrudService<K, E> {

    @Nonnull
    protected ServiceExceptionMapper sem;

    @Nonnull
    protected LogLevel exceptionLogLevel;

    public AbstractCrudService(@Nonnull ServiceExceptionMapper sem, @Nonnull LogLevel exceptionLogLevel) {
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
        return "AbstractCrudService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
