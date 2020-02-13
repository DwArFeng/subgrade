package com.dwarfeng.subgrade.sdk.service.custom.operation;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 服务的增删改查操作。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public interface CrudOperation<K extends Key, E extends Entity<K>> {

    /**
     * 判断指定的对象是否存在
     *
     * @param key 指定的对象对应的键。
     * @return 指定的对象是否存在。
     * @throws Exception 操作过程中的任何异常。
     */
    boolean exists(K key) throws Exception;

    /**
     * 获取指定主键对应的实体。
     * <p>实现该方法时需要判断指定的主键是否存在，不过不存在，请抛出异常或者返回null。</p>
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的实体。
     * @throws Exception 操作过程中的任何异常。
     */
    E get(K key) throws Exception;

    /**
     * 插入指定的元素。
     * <p>该方法被调用时可以保证入口参数拥有服务中不存在的主键。</p>
     *
     * @param element 指定的元素，可以保证其主键不为null且服务中不存在。
     * @return 插入后元素的主键或分配的新主键。
     * @throws Exception 操作过程中的任何异常。
     */
    K insert(E element) throws Exception;

    /**
     * 更新指定的元素。
     * <p>该方法被调用时可以保证入口参数拥有服务中存在的主键。</p>
     *
     * @param element 指定的元素，可以保证其主键不为null且服务中存在。
     * @throws Exception 操作过程中的任何异常。
     */
    void update(E element) throws Exception;

    /**
     * 删除指定的元素。
     * <p>该方法被调用时可以保证服务中存在入口参数。</p>
     *
     * @param key 指定的元素对应的主键，可以保证不为null且服务中存在。
     * @throws Exception 操作过程中的任何异常。
     */
    void delete(K key) throws Exception;
}
