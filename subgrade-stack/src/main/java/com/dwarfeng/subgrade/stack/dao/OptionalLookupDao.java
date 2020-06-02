package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * 可选查询数据访问层。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface OptionalLookupDao<E extends Entity<?>> extends Dao {

    /**
     * 查询数据访问层中满足可选条件的所有对象。
     *
     * <p>
     * 不同的元素使用逻辑与进行拼接。
     *
     * @param optional 指定的可选条件。
     * @return 数据访问层中满足可选条件的所有对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(Map<String, Object[]> optional) throws DaoException;

    /**
     * 查询数据访问层中满足可选条件的所有对象。
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
     * @return 数据访问层中满足可选条件的所有对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional) throws DaoException;

    /**
     * 查询数据访问层中满足可选条件的对象，附加分页条件。
     *
     * <p>
     * 不同的元素使用逻辑与进行拼接。
     *
     * @param optional   指定的可选条件。
     * @param pagingInfo 分页信息。
     * @return 数据访问层中满足可选条件的对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(Map<String, Object[]> optional, PagingInfo pagingInfo) throws DaoException;

    /**
     * 查询数据访问层中满足可选条件的对象，附加分页条件。
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
     * @return 数据访问层中满足可选条件的对象。
     * @throws DaoException 数据访问层异常。
     */
    List<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional,
            PagingInfo pagingInfo) throws DaoException;

    /**
     * 查询数据访问层中满足可选条件的对象的数量。
     *
     * <p>
     * 不同的元素使用逻辑与进行拼接。
     *
     * @param optional 指定的可选条件。
     * @return 数据访问层中满足可选条件的对象的数量。
     * @throws DaoException 数据访问异常。
     */
    int lookupCount(Map<String, Object[]> optional) throws DaoException;

    /**
     * 查询数据访问层中满足可选条件的对象的数量。
     *
     * <p>
     * <code>andOptional</code> 中的元素使用逻辑与拼接。<br>
     * <code>orOptional</code> 列表中的单个列表元素内部使用逻辑或进行拼接；不同元素之间使用逻辑与拼接。<br>
     * <code>notOptional</code> 列表中元素取逻辑非后使用逻辑与拼接。<br>
     * 不同的实体对象使用AND进行拼接。
     *
     * @param andOptional 可选条件：与。
     * @param orOptional  可选条件：或。
     * @param notOptional 可选条件：非。
     * @return 数据访问层中满足可选条件的对象的数量。
     * @throws DaoException 数据访问异常。
     */
    int lookupCount(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional) throws DaoException;
}
