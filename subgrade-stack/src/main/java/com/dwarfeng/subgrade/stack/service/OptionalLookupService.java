package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * 可选查询服务。
 *
 * <p>
 * 可选查询服务用于针对某一实体对象进行可选条件的查询。可选查询在复杂的查询条件下被使用，支持任意种类的单样板拼接。
 * <br>
 * 比起样板查询 {@link PresetLookupService}，可选查询与其最大的不同是：样板查询针对一个实体对象，提供有限的样板式查询，
 * 其中单个样板可能会涉及到多条件的查询；而可选查询力求每个样板简单，功能单一，最好限制到一个字段，然后用户可以任意选取
 * 一个或多个模板进行逻辑拼接，实现无限的样式查询。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface OptionalLookupService<E extends Entity<?>> extends Service {

    /**
     * 根据指定的可选条件查询实体对象。
     *
     * <p>
     * 不同的元素使用逻辑与进行拼接。
     *
     * @param optional 可选条件。
     * @return 满足查询条件的所有实体对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(Map<String, Object[]> optional) throws ServiceException;

    /**
     * 根据指定的可选条件查询实体对象。
     *
     * <p>
     * <code>andOptional</code> 中的元素使用逻辑与拼接。<br>
     * <code>orOptional</code> 列表中的单个列表元素内部使用逻辑或进行拼接；不同元素之间使用逻辑与拼接。<br>
     * <code>notOptional</code> 列表中元素取逻辑非后使用逻辑与拼接。<br>
     * 不同的实体对象使用AND进行拼接。
     *
     * @param andOptional   可选条件：与。
     * @param orOptional    可选条件：或。
     * @param notOptional   可选条件：非。
     * @param orderOptional 可选条件：排序。
     * @return 满足查询条件的所有实体对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional) throws ServiceException;

    /**
     * 根据指定的可选条件查询实体对象，附加分页条件。
     *
     * <p>
     * 不同的元素使用逻辑与进行拼接。
     *
     * @param optional   可选条件。
     * @param pagingInfo 分页信息。
     * @return 带有分页信息的满足查询条件的所有实体对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(Map<String, Object[]> optional, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 根据指定的可选条件查询实体对象，附加分页条件。
     *
     * <p>
     * <code>andOptional</code> 中的元素使用逻辑与拼接。<br>
     * <code>orOptional</code> 列表中的单个列表元素内部使用逻辑或进行拼接；不同元素之间使用逻辑与拼接。<br>
     * <code>notOptional</code> 列表中元素取逻辑非后使用逻辑与拼接。<br>
     * 不同的实体对象使用AND进行拼接。
     *
     * @param andOptional   可选条件：与。
     * @param orOptional    可选条件：或。
     * @param notOptional   可选条件：非。
     * @param orderOptional 可选条件：排序。
     * @param pagingInfo    分页信息。
     * @return 带有分页信息的满足查询条件的所有实体对象。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional,
            PagingInfo pagingInfo) throws ServiceException;
}
