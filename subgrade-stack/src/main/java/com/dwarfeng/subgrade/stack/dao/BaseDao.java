package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 基础数据访问层
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface BaseDao<K extends Key, E extends Entity<K>> extends Dao {

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

    /**
     * 插入实体。
     *
     * @param element 指定的实体。
     * @return 指定的实体对应的主键。
     */
    K insert(E element) throws DaoException;

    /**
     * 更新实体。
     *
     * @param element 更新的实体。
     * @return 更新实体对应的键。
     */
    K update(E element) throws DaoException;

    /**
     * 删除实体。
     *
     * @param key 实体的键。
     */
    void delete(K key) throws DaoException;
}