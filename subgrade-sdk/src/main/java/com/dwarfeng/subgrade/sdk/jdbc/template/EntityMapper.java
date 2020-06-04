package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 实体映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface EntityMapper<K extends Key, E extends Entity<K>> {

    /**
     * 将指定的对象数组映射为主键。
     *
     * @param objects 指定的对象数组。
     * @return 映射形成的主键。
     */
    K objects2Key(Object[] objects);

    /**
     * 将指定的主键映射为对象数组。
     *
     * @param key 指定的主键。
     * @return 映射形成的对象数组。
     */
    Object[] key2Objects(K key);

    /**
     * 将指定的对象数组映射为实体。
     *
     * @param objects 指定的对象数组。
     * @return 映射形成的实体。
     */
    E objects2Entity(Object[] objects);

    /**
     * 将指定的实体映射为对象数组。
     *
     * @param entity 指定的实体。
     * @return 映射形成的对象数组。
     */
    Object[] entity2Objects(E entity);
}
