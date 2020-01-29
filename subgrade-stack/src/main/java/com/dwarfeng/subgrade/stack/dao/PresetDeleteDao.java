package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 可以预设查询并删除的数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.6-beta
 */
public interface PresetDeleteDao<E extends Entity<?>> extends PresetLookupDao<E> {

    /**
     * 删除数据访问层中符合预设的所有对象。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设相关的对象。
     * @throws DaoException 数据访问异常。
     */
    void lookupDelete(String preset, Object[] objs) throws DaoException;
}
