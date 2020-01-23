package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 查询数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface LookupDao<E extends Entity<?>> extends Dao {

    /**
     * 查询数据访问层中的所有元素。
     *
     * @return 数据访问层中的所有元素组成的列表。
     * @throws DaoException 数据访问异常。
     */
    List<E> lookup() throws DaoException;

    /**
     * 查询数据访问层中的分页元素。
     *
     * @param pagingInfo 元素的分页信息。
     * @return 指定页数上的所有元素。
     * @throws DaoException 数据访问异常。
     */
    List<E> lookup(PagingInfo pagingInfo) throws DaoException;

    /**
     * 查询数据访问层中的元素的数量。
     *
     * @return 数据访问层中的元素的数量。
     * @throws DaoException 数据访问异常。
     */
    int lookupCount() throws DaoException;
}
