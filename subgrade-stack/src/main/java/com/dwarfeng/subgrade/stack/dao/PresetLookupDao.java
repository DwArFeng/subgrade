package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 预设查询数据访问层。
 * <p>该数据访问层支持有限个自定义的预设查询。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface PresetLookupDao<E extends Entity<?>> extends Dao {

    /**
     * 查询数据访问层中满足指定预设的所有对象。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 数据访问层中满足指定预设的所有对象。
     * @throws DaoException 数据访问异常。
     */
    List<E> lookup(String preset, Object[] objs) throws DaoException;

    /**
     * 查询数据访问层中满足预设的对象。
     *
     * @param preset     指定的预设名称。
     * @param objs       预设对应的对象数组。
     * @param pagingInfo 分页信息。
     * @return 数据访问层中满足预设的对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException;

    /**
     * 查询数据访问层中满足预设的对象的数量。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 数据访问层中满足预设的对象的数量。
     * @throws DaoException 数据访问异常。
     */
    int lookupCount(String preset, Object[] objs) throws DaoException;
}
