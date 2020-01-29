package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 单对象数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface SingleObjectDao<E extends Entity<?>> extends Dao {

    /**
     * 判断单个实体是否存在。
     *
     * @return 单个实体是否存在。
     * @throws DaoException 数据访问层异常。
     */
    boolean exists() throws DaoException;

    /**
     * 获取单个对象。
     *
     * @return 单个对象的实体。
     * @throws DaoException 数据访问异常。
     */
    E get() throws DaoException;

    /**
     * 插入或更新指定的实体。
     *
     * @param entity 指定的实体。
     * @throws DaoException 数据访问异常。
     */
    void put(E entity) throws DaoException;

    /**
     * 删除指定的实体。
     *
     * @throws DaoException 数据访问异常。
     */
    void clear() throws DaoException;
}
