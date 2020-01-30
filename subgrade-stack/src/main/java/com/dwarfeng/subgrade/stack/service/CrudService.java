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
     * 获取指定的实体是否存在。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体。
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
     * @param element 指定的实体。
     * @return 指定的实体对应的主键。
     * @throws ServiceException 服务异常。
     */
    K insert(E element) throws ServiceException;

    /**
     * 更新实体。
     *
     * @param element 更新的实体。
     * @throws ServiceException 服务异常。
     */
    void update(E element) throws ServiceException;

    /**
     * 删除实体。
     *
     * @param key 实体的键。
     * @throws ServiceException 服务异常。
     */
    void delete(K key) throws ServiceException;
}
