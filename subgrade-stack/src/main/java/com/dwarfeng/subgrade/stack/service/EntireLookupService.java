package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

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
     * 查询所有元素，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup()} 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup()} 相同，需要实现类重写，以满足到提高效率的需求。
     *
     * @return 元素组成的列表。
     * @throws ServiceException 服务异常。
     * @since 1.2.4
     */
    default List<E> lookupAsList() throws ServiceException {
        return lookup().getData();
    }

    /**
     * 查询分页元素。
     *
     * @param pagingInfo 元素的分页信息。
     * @return 带有分页信息的指定页上的元素。
     * @throws ServiceException 服务异常。
     */
    PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException;

    /**
     * 查询分页元素，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup(PagingInfo)}
     * 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(PagingInfo)} 相同，需要实现类重写，以满足到提高效率的需求。
     *
     * @param pagingInfo 分页信息。
     * @return 元素组成的列表。
     * @throws ServiceException 服务异常。
     * @since 1.2.4
     */
    default List<E> lookupAsList(PagingInfo pagingInfo) throws ServiceException {
        return lookup(pagingInfo).getData();
    }

    /**
     * 查询数据访问层中的第一个元素。
     *
     * <p>
     * 当数据访问层中存在数据时，返回第一个数据；当数据访问层中不存在数据时，返回 null。
     *
     * @return 数据访问层中的第一个对象，或者是 null。
     * @throws ServiceException 服务异常。
     * @since 1.2.8
     */
    default E lookupFirst() throws ServiceException {
        return lookup(PagingInfo.FIRST_ONE).getData().stream().findFirst().orElse(null);
    }
}
