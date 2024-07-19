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
     * 批量查询实体是否存在。
     *
     * <p>
     * 如果指定的实体键列表中的所有键对应的实体全部存在，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param keys 实体的键组成的列表。
     * @return 指定的主键是否全部存在。
     * @throws ServiceException 服务异常。
     */
    boolean allExists(List<K> keys) throws ServiceException;

    /**
     * 批量查询实体是否存在。
     *
     * <p>
     * 如果指定的实体键列表中的所有键对应的实体全部不存在，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param keys 实体的键组成的列表。
     * @return 指定的主键是否全部不存在。
     * @throws ServiceException 服务异常。
     */
    boolean nonExists(List<K> keys) throws ServiceException;

    /**
     * 批量获取实体。
     *
     * @param keys 实体的键组成的列表。
     * @return 实体的键对应的实体组成的列表。
     * @throws ServiceException 服务异常。
     */
    List<E> batchGet(List<K> keys) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    List<K> batchInsert(List<E> entities) throws ServiceException;

    /**
     * 批量更新实体。
     *
     * @param entities 实体组成的列表
     * @throws ServiceException 服务异常。
     */
    void batchUpdate(List<E> entities) throws ServiceException;

    /**
     * 批量删除实体。
     *
     * @param keys 实体的键组成的列表。
     * @throws ServiceException 服务异常。
     */
    void batchDelete(List<K> keys) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    List<E> batchGetIfExists(List<K> keys) throws ServiceException;

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
     * @return 所有被插入实体的主键，小于或等于 entities 的个数。
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
    List<K> batchInsertIfExists(List<E> entities) throws ServiceException;

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
     * @return 所有被插入实体的主键，小于或等于 entities 的个数。
     * @throws ServiceException 服务异常。
     */
    default List<K> batchInsertIfNotExists(List<E> entities) throws ServiceException {
        return batchInsertIfExists(entities);
    }

    /**
     * 批量更新实体。
     *
     * <p>
     * 对于批量操作的每一个实体，如果实体存在，则更新；否则不做任何处理。
     *
     * @param entities 实体组成的列表
     * @throws ServiceException 服务异常。
     */
    void batchUpdateIfExists(List<E> entities) throws ServiceException;

    /**
     * 批量删除实体。
     *
     * <p>
     * 对于批量操作的每一个键，如果对应的实体存在，则删除；否则不做任何处理。
     *
     * @param keys 实体的键组成的列表。
     * @throws ServiceException 服务异常。
     */
    void batchDeleteIfExists(List<K> keys) throws ServiceException;

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
     * @throws ServiceException 服务异常。
     */
    List<K> batchInsertOrUpdate(List<E> entities) throws ServiceException;
}
