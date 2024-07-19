package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 实体增删改查服务。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface CrudService<K extends Key, E extends Entity<K>> extends Service {

    /**
     * 查询实体是否存在。
     *
     * @param key 实体的键。
     * @return 实体是否存在。
     * @throws ServiceException 服务异常。
     */
    boolean exists(K key) throws ServiceException;

    /**
     * 获取实体。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体。
     * @throws ServiceException 服务异常。
     */
    E get(K key) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    K insert(E entity) throws ServiceException;

    /**
     * 更新实体。
     *
     * @param entity 实体。
     * @throws ServiceException 服务异常。
     */
    void update(E entity) throws ServiceException;

    /**
     * 删除实体。
     *
     * @param key 实体的键。
     * @throws ServiceException 服务异常。
     */
    void delete(K key) throws ServiceException;

    /**
     * 获取实体。
     *
     * <p>
     * 如果存在指定的键，则获取实体；否则返回 <code>null</code>。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体，或者是 null。
     * @throws ServiceException 服务异常。
     */
    E getIfExists(K key) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    K insertIfNotExists(E entity) throws ServiceException;

    /**
     * 更新实体。
     *
     * <p>
     * 如果存在指定的实体，则更新实体；否则不做任何处理。
     *
     * @param entity 实体。
     * @throws ServiceException 服务异常。
     */
    void updateIfExists(E entity) throws ServiceException;

    /**
     * 删除实体。
     *
     * <p>
     * 如果存在指定的实体，则删除实体；否则不做任何处理。
     *
     * @param key 实体的键。
     * @throws ServiceException 服务异常。
     */
    void deleteIfExists(K key) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    K insertOrUpdate(E entity) throws ServiceException;
}
