package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.dto.LookupPagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

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

    /**
     * 获取数据访问层的全部实体。
     *
     * @return 数据访问层中的所有实体。
     * @throws DaoException 数据访问层异常。
     */
    List<E> getAll() throws DaoException;

    /**
     * 获取数据访问层中实体的总数。
     *
     * @return 实体的总数。
     * @throws DaoException 数据访问层异常。
     */
    int countAll() throws DaoException;

    /**
     * 获取数据访问层的实体。
     *
     * @param lookupPagingInfo 查询分页信息。
     * @return 分页的实体。
     * @throws DaoException 数据访问层异常。
     */
    List<E> get(LookupPagingInfo lookupPagingInfo) throws DaoException;
}
