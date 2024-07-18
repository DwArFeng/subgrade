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
     * @throws ServiceException 服务异常。
     */
    void batchUpdate(List<E> elements) throws ServiceException;

    /**
     * 批量删除实体。
     *
     * @param keys 实体组成的列表。
     * @throws ServiceException 服务异常。
     */
    void batchDelete(List<K> keys) throws ServiceException;

    /**
     * 批量获取指定且存在的键对应的元素。
     *
     * @param keys 指定的键组成的列表。
     * @return 指定且存在的键对应的元素组成的列表，元素的个数小于等于键列表的个数。
     * @throws ServiceException 服务异常。
     */
    List<E> batchGetIfExists(List<K> keys) throws ServiceException;

    /**
     * 如果不存在指定的元素，则插入。
     *
     * @param elements 指定的元素组成的集合。
     * @return 所有被插入元素的主键，小于或等于 elements 的个数。
     * @throws ServiceException 服务异常。
     * @see #batchInsertIfNotExists(List)
     * @deprecated 该方法不符合语义，不建议使用。请使用 {@link #batchInsertIfNotExists(List)}。
     */
    /*
     * 为了满足接口的兼容性，该接口中的 batchInsertIfNotExists 方法的默认实现是调用本方法，
     * 因此忽略 DeprecatedIsStillUsed 警告。
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    List<K> batchInsertIfExists(List<E> elements) throws ServiceException;

    /**
     * 如果不存在指定的元素，则插入。
     *
     * @param elements 指定的元素组成的集合。
     * @return 所有被插入元素的主键，小于或等于 elements 的个数。
     * @throws ServiceException 服务异常。
     */
    default List<K> batchInsertIfNotExists(List<E> elements) throws ServiceException {
        return batchInsertIfExists(elements);
    }

    /**
     * 如果存在指定的元素，则更新。
     *
     * @param elements 指定的元素组成的集合。
     * @throws ServiceException 服务异常。
     */
    void batchUpdateIfExists(List<E> elements) throws ServiceException;

    /**
     * 如果存在指定的元素，则删除。
     *
     * @param keys 指定的元素对应的键组成的集合。
     * @throws ServiceException 服务异常。
     */
    void batchDeleteIfExists(List<K> keys) throws ServiceException;

    /**
     * 如果元素不存在，则插入；存在，则更新。
     *
     * @param elements 指定的元素组成的集合。
     * @return 所有被插入元素的主键，小于或等于 elements 的个数。
     * @throws ServiceException 服务异常。
     */
    List<K> batchInsertOrUpdate(List<E> elements) throws ServiceException;
}
