package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

/**
 * 批量写入服务。
 *
 * <p>
 * 写入服务 {@link WriteService} 的批量版本。
 *
 * @author DwArFeng
 * @see WriteService
 * @since 1.1.0
 */
public interface BatchWriteService<E extends Entity<?>> extends WriteService<E> {

    /**
     * 写入指定的元素。
     *
     * @param elements 指定的元素组成的列表。
     * @throws ServiceException 服务异常。
     * @see WriteService#write(Entity)
     */
    void batchWrite(List<E> elements) throws ServiceException;
}
