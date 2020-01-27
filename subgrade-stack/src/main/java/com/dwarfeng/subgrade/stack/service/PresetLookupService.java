package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 预设查询服务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface PresetLookupService<E extends Entity<?>> extends Service {

    /**
     * 查询数据访问层中满足指定预设的所有对象。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 带有分页信息的数据访问层中满足指定预设的所有对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(String preset, Object[] objs) throws ServiceException;

    /**
     * 查询数据访问层中满足预设的对象。
     *
     * @param preset     指定的预设名称。
     * @param objs       预设对应的对象数组。
     * @param pagingInfo 分页信息。
     * @return 带有分页信息的数据访问层中满足预设的对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws ServiceException;
}
