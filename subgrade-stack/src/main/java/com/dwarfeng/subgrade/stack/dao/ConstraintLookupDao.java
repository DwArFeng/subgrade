package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * 具有约束的查询访问层。
 * TODO 约束的形式待定，请暂时不要使用该类。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface ConstraintLookupDao<E extends Entity<?>> extends Dao {

    /**
     * 查询数据访问层中满足约束条件的所有对象。
     *
     * @param constraint 指定的约束条件。
     * @return 数据访问层中满足约束条件的所有对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(Map<String, Object> constraint) throws DaoException;

    /**
     * 查询数据访问层中满足约束条件的对象。
     *
     * @param constraint 指定的约束条件。
     * @param pagingInfo 分页信息。
     * @return 数据访问层中满足约束条件的对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(Map<String, Object> constraint, PagingInfo pagingInfo) throws DaoException;

    /**
     * 查询数据访问层中满足约束条件的对象的数量。
     *
     * @param constraint 指定的约束条件。
     * @return 数据访问层中满足约束条件的对象的数量。
     * @throws DaoException 数据访问异常。
     */
    int lookupCount(Map<String, Object> constraint) throws DaoException;
}
