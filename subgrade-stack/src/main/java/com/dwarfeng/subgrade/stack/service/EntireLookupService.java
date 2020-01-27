package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 全体查询服务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface EntireLookupService<E extends Entity<?>> extends Service {

    /**
     * 查询所有元素。
     *
     * @return 带有分页信息的所有元素。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup() throws ServiceException;

    /**
     * 查询分页元素。
     *
     * @param pagingInfo 元素的分页信息。
     * @return 带有分页信息的指定页上的元素。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException;
}
