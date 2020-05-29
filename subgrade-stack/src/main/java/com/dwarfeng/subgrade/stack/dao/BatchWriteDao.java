package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 批量写入数据访问层接口。
 *
 * <p>
 * 数据访问层接口 {@link WriteDao} 的批量版本。
 *
 * @author DwArFeng
 * @see WriteDao
 * @since 1.1.0
 */
public interface BatchWriteDao<E extends Entity<?>> extends WriteDao<E> {

    /**
     * 写入指定的元素。
     *
     * @param elements 指定的元素组成的列表。
     * @throws DaoException 数据访问层异常。
     * @see WriteDao#write(Entity)
     */
    void batchWrite(List<E> elements) throws DaoException;
}
