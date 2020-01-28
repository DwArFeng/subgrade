package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 只读批量数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ReadOnlyBatchDao<K extends Key, E extends Entity<K>> extends Dao {

    /**
     * 查询指定的键是否全部存在。
     *
     * @param keys 指定的键组成的集合。
     * @return 指定的键是否全部存在。
     * @throws DaoException 数据访问层异常。
     */
    boolean allExists(List<K> keys) throws DaoException;

    /**
     * 查询指定的键是否全部不存在。
     *
     * @param keys 指定的键组成的集合。
     * @return 指定的键是否全部不存在。
     * @throws DaoException 数据访问层异常。
     */
    boolean nonExists(List<K> keys) throws DaoException;

    /**
     * 批量获取指定的键对应的实体。
     *
     * @param keys 指定的键组成的列表。
     * @return 指定的键对应的实体组成的列表。
     * @throws DaoException 数据访问层异常。
     */
    List<E> batchGet(List<K> keys) throws DaoException;
}
