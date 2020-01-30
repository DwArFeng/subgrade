package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 可以预设查询并删除的数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.6-beta
 */
public interface PresetDeleteDao<K extends Key, E extends Entity<K>> extends PresetLookupDao<E> {

    /**
     * 删除数据访问层中符合预设的所有对象。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设相关的对象。
     * @return 被删除的数据的组件组成的列表。
     * @throws DaoException 数据访问异常。
     */
    List<K> lookupDelete(String preset, Object[] objs) throws DaoException;
}
