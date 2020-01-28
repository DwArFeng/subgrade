package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 可批量操作的基础数据访问层
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface BatchBaseDao<K extends Key, E extends Entity<K>> extends BaseDao<K, E>, ReadOnlyBatchDao<K, E> {

    /**
     * 批量插入实体。
     *
     * @param elements 实体组成的列表。
     * @return 插入的实体对应的主键。
     * @throws DaoException 数据访问层异常。
     */
    List<K> batchInsert(List<E> elements) throws DaoException;

    /**
     * 批量更新实体。
     *
     * @param elements 实体组成的列表。
     * @return 更新的实体对应的主键。
     * @throws DaoException 数据访问层异常。
     */
    List<K> batchUpdate(List<E> elements) throws DaoException;

    /**
     * 批量删除实体。
     *
     * @param keys 实体组成的列表。
     * @throws DaoException 数据访问层异常。
     */
    void batchDelete(List<K> keys) throws DaoException;
}
