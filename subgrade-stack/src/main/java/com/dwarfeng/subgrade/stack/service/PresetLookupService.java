package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

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
     * 查询数据访问层中满足指定预设的所有对象，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup(String, Object[])}
     * 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(String, Object[])} 相同，需要实现类重写，以满足到提高效率的需求。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 元素组成的列表。
     * @throws ServiceException 服务异常。
     * @since 1.2.4
     */
    default List<E> lookupAsList(String preset, Object[] objs) throws ServiceException {
        return lookup(preset, objs).getData();
    }

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

    /**
     * 查询数据访问层中满足指定预设的所有对象，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup(String, Object[], PagingInfo)}
     * 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(String, Object[], PagingInfo)} 相同，
     * 需要实现类重写，以满足到提高效率的需求。
     *
     * @param preset     指定的预设名称。
     * @param objs       预设对应的对象数组。
     * @param pagingInfo 分页信息。
     * @return 元素组成的列表。
     * @throws ServiceException 服务异常。
     * @since 1.2.4
     */
    default List<E> lookupAsList(String preset, Object[] objs, PagingInfo pagingInfo) throws ServiceException {
        return lookup(preset, objs, pagingInfo).getData();
    }

    /**
     * 查询某个预设的第一个元素。
     *
     * <p>
     * 当预设中存在数据时，返回第一个数据；当预设中不存在数据时，返回 null。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 数据访问层中满足预设的第一个对象，或者是 null。
     * @throws ServiceException 服务异常。
     * @since 1.2.8
     */
    default E lookupFirst(String preset, Object[] objs) throws ServiceException {
        return lookup(preset, objs, PagingInfo.FIRST_ONE).getData().stream().findFirst().orElse(null);
    }

    /**
     * 查询数据访问层中满足指定预设的所有对象的总数量。
     *
     * <p>
     * 在该接口的大量实践中发现，许多场景只需要获取元素的数量，而不需要获取元素列表。
     * 使用 {@link #lookup(String, Object[])} 或 {@link #lookup(String, Object[], PagingInfo)}
     * 方法查询会造成不必要的性能浪费。<br>
     * 该方法可以直接返回数据总量，在部分场景下使用该方法可以提高查询的效率。
     *
     * <p>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(String, Object[], PagingInfo)} 相同，需要实现类重写，
     * 以满足到提高效率的需求。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设对应的对象数组。
     * @return 元素的数量。
     * @throws ServiceException 服务异常。
     * @since 1.4.1
     */
    default int lookupCount(String preset, Object[] objs) throws ServiceException {
        return (int) lookup(preset, objs, PagingInfo.FIRST_ONE).getCount();
    }
}
