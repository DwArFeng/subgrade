package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

/**
 * 实体增删改查服务。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface BatchCrudService<K extends Key, E extends Entity<K>> extends CrudService<K, E> {


    /**
     * 查询指定的主键是否全部存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部存在。
     * @throws ServiceException 服务异常。
     */
    boolean allExists(List<K> keys) throws ServiceException;

    /**
     * 查询指定的主键是否全部不存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部不存在。
     * @throws ServiceException 服务异常。
     */
    boolean nonExists(List<K> keys) throws ServiceException;

    /**
     * 批量获取指定的键对应的元素。
     *
     * @param keys 指定的键组成的列表。
     * @return 指定的键对应的元素组成的列表。
     * @throws ServiceException 服务异常。
     */
    List<E> batchGet(List<K> keys) throws ServiceException;

    /**
     * 批量插入实体。
     *
     * @param elements 实体组成的列表。
     * @return 插入的实体对应的主键。
     * @throws ServiceException 服务异常。
     */
    List<K> batchInsert(List<E> elements) throws ServiceException;

    /**
     * 批量更新实体。
     *
     * @param elements 实体组成的列表。
     * @return 更新的实体对应的主键。
     * @throws ServiceException 服务异常。
     */
    List<K> batchUpdate(List<E> elements) throws ServiceException;

    /**
     * 批量删除实体。
     *
     * @param keys 实体组成的列表。
     * @throws ServiceException 服务异常。
     */
    void batchDelete(List<K> keys) throws ServiceException;
}
