package com.dwarfeng.subgrade.sdk.service.custom.operation;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.List;

/**
 * 服务的批量增删改查操作。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public interface BatchCrudOperation<K extends Key, E extends Entity<K>> extends CrudOperation<K, E> {

    /**
     * 判断指定的主键是否全部存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部存在。
     * @throws Exception 操作过程中的任何异常。
     */
    boolean allExists(List<K> keys) throws Exception;

    /**
     * 判断指定的主键是否全部不存在。
     *
     * @param keys 指定的主键组成的列表。
     * @return 指定的主键是否全部不存在。
     * @throws Exception 操作过程中的任何异常。
     */
    boolean nonExists(List<K> keys) throws Exception;

    /**
     * 获取指定主键对应的实体。
     * <p>实现该方法时需要判断指定的主键是否存在，不过不存在，请抛出异常或者返回null。</p>
     *
     * @param keys 指定的主键。
     * @return 指定的主键对应的实体。
     * @throws Exception 操作过程中的任何异常。
     */
    List<E> batchGet(List<K> keys) throws Exception;

    /**
     * 插入指定的元素。
     * <p>该方法被调用时可以保证入口参数中的全部元素拥有服务中不存在的主键。</p>
     *
     * @param elements 指定的元素组成的列表，可以保证其中所有的元素主键不为null且服务中不存在。
     * @return 插入后元素的主键或分配的新主键。
     * @throws Exception 操作过程中的任何异常。
     */
    List<K> batchInsert(List<E> elements) throws Exception;

    /**
     * 更新指定的元素。
     * <p>该方法被调用时可以保证入口参数中所有的元素拥有服务中存在的主键。</p>
     *
     * @param elements 指定的元素，可以保证其中的所有元素主键不为null且服务中存在。
     * @throws Exception 操作过程中的任何异常。
     */
    void batchUpdate(List<E> elements) throws Exception;

    /**
     * 删除指定的元素。
     * <p>该方法被调用时可以保证入口参数中所有的主键存在。</p>
     *
     * @param keys 指定的元素对应的主键，可以保证其中的所有主键不为null且服务中存在。
     * @throws Exception 操作过程中的任何异常。
     */
    void batchDelete(List<K> keys) throws Exception;
}
