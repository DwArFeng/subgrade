package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 只读数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ReadOnlyDao<K extends Key, E extends Entity<K>> extends Dao {

    /**
     * 获取指定的实体是否存在。
     *
     * @param key 指定的实体的键。
     * @return 指定的实体是否存在。
     * @throws DaoException 数据访问层异常。
     */
    boolean exists(K key) throws DaoException;

    /**
     * 获取实体。
     *
     * @param key 实体的键。
     * @return 实体的键对应的实体。
     */
    E get(K key) throws DaoException;
}
