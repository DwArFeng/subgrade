package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 单对象服务。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public interface SingleObjectService<E extends Entity<?>> extends Service {

    /**
     * 判断单个实体是否存在。
     *
     * @return 单个实体是否存在。
     * @throws ServiceException 数据访问层异常。
     */
    boolean exists() throws ServiceException;

    /**
     * 获取单个对象。
     *
     * @return 单个对象的实体。
     * @throws ServiceException 服务异常。
     */
    E get() throws ServiceException;

    /**
     * 插入或更新指定的实体。
     *
     * @param entity 指定的实体。
     * @throws ServiceException 服务异常。
     */
    void put(E entity) throws ServiceException;

    /**
     * 删除指定的实体。
     *
     * @throws ServiceException 服务异常。
     */
    void clear() throws ServiceException;
}
