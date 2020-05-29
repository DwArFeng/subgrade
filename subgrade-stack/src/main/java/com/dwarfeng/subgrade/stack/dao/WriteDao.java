package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 写入数据访问层接口。
 *
 * <p>
 * 写入数据访问层接口是用在大量新数据写入场景下的，如数据采集。<br>
 * 该接口是服务 {@link com.dwarfeng.subgrade.stack.service.WriteService} 的配套接口。
 *
 * @author DwArFeng
 * @see com.dwarfeng.subgrade.stack.service.WriteService
 * @since 1.1.0
 */
public interface WriteDao<E extends Entity<?>> extends Dao {

    /**
     * 写入指定的元素。
     * <p>
     * 写入的元素应该遵守如下的规则：
     * <pre>
     * 1. 元素的主键允许为 null。
     * 2. 主键不为 null 的元素必须确保之前不存在。
     * </pre>
     *
     * @param element 指定的元素。
     * @throws DaoException 数据访问层异常。
     */
    void write(E element) throws DaoException;
}
