package com.dwarfeng.subgrade.sdk.jdbc.mapper;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * Crud映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface CrudMapper<K extends Key, E extends Entity<K>> {

    /**
     * 提供插入数据用的参数数组。
     *
     * @return 插入数据用的参数数组。
     */
    Object[] insert2Args(E entity);

    /**
     * 提供更新数据用的参数数组。
     *
     * @return 更新数据用的参数数组。
     */
    Object[] update2Args(E entity);

    /**
     * 提供删除数据用的参数数组。
     *
     * @return 删除数据用的参数数组。
     */
    Object[] delete2Args(K key);

    /**
     * 提供获取数据用的参数数组。
     *
     * @return 获取数据用的参数数组。
     */
    Object[] get2Args(K key);

    /**
     * 提供判断数据是否存在用的参数数组。
     *
     * <p>
     * 该参数数组需要满足：当数据存在时，返回不少于 1 条结果（什么结果都行）；当数据不存在时，返回 0 条结果。<br>
     * 例如: <code>SELECT 1 FROM table_name WHERE id='specific_value'</code>
     *
     * @return 判断数据是否存在用的参数数组。
     */
    Object[] exists2Args(K key);
}
