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
     * <p>
     * 在 1.5.0 版本中，增加了对每页行数为 0 的分页信息的支持，当每页行数为 0 时，仅查询数据总量，不返回数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>当前页</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>-1</td>
     *         <td>由于每页行数为 0，总页数无法计算，使用负数。</td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>0</td>
     *         <td>常量值，为 0。</td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>空列表</td>
     *         <td>由于每页行数为 0，故返回空列表。</td>
     *     </tr>
     * </table>
     *
     * <p>
     * 在该接口的实践中发现，
     * 部分开发人员习惯使用每页行数小于 0 的分页信息（即 {@link PagingInfo#getRows()} 为负数）表示不分页。<br>
     * 此特性在 1.5.0 版本中得到了正式的支持，当每页行数小于 0 时，查询全部数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>当前页</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>1</td>
     *         <td>所有数据都在第 0 页里，共 1 页。</td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>Integer.MAX_VALUE</td>
     *         <td>等价于每页 Integer.MAX_VALUE 行。</td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>所有对象组成的列表或空列表</td>
     *         <td>当 currentPage 为 0 时，data 包含所有数据<br>当 currentPage 大于 0 时，data 为空列表。</td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
     *
     * <p>
     * 在该接口的实践中发现，
     * 部分开发人员在调用此接口时错误地使用页数小于 0 的分页信息（即 {@link PagingInfo#getPage()} 为负数）。
     * 在这种情况下，由于接口实现以及初始化参数的不同，执行此方法可能会导致不同的结果。<br>
     * 在 1.5.0 版本中，增加了对页数小于 0 的分页信息的支持，当页数小于 0 时，查询第 0 页的数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>0</td>
     *         <td>等价于第 0 页</td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>总页数</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>每页行数</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>所有对象组成的列表</td>
     *         <td></td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
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
     * <p>
     * 在 1.5.0 版本中，增加了对每页行数为 0 的分页信息的支持，当每页行数为 0 时，仅查询数据总量，不返回数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>当前页</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>-1</td>
     *         <td>由于每页行数为 0，总页数无法计算，使用负数。</td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>0</td>
     *         <td>常量值，为 0。</td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>空列表</td>
     *         <td>由于每页行数为 0，故返回空列表。</td>
     *     </tr>
     * </table>
     *
     * <p>
     * 在该接口的实践中发现，
     * 部分开发人员习惯使用每页行数小于 0 的分页信息（即 {@link PagingInfo#getRows()} 为负数）表示不分页。<br>
     * 此特性在 1.5.0 版本中得到了正式的支持，当每页行数小于 0 时，查询全部数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>当前页</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>1</td>
     *         <td>所有数据都在第 0 页里，共 1 页。</td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>Integer.MAX_VALUE</td>
     *         <td>等价于每页 Integer.MAX_VALUE 行。</td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>所有对象组成的列表或空列表</td>
     *         <td>当 currentPage 为 0 时，data 包含所有数据<br>当 currentPage 大于 0 时，data 为空列表。</td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
     *
     * <p>
     * 在该接口的实践中发现，
     * 部分开发人员在调用此接口时错误地使用页数小于 0 的分页信息（即 {@link PagingInfo#getPage()} 为负数）。
     * 在这种情况下，由于接口实现以及初始化参数的不同，执行此方法可能会导致不同的结果。<br>
     * 在 1.5.0 版本中，增加了对页数小于 0 的分页信息的支持，当页数小于 0 时，查询第 0 页的数据，
     * 返回的 {@link PagedData} 将会遵循以下规则：
     * <table>
     *     <tr>
     *         <th>字段</th>
     *         <th>值或值的规则</th>
     *         <th>备注</th>
     *     </tr>
     *     <tr>
     *         <td>currentPage</td>
     *         <td>0</td>
     *         <td>等价于第 0 页</td>
     *     </tr>
     *     <tr>
     *         <td>totalPages</td>
     *         <td>总页数</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>rows</td>
     *         <td>每页行数</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>count</td>
     *         <td>所有实体的数量</td>
     *         <td></td>
     *     </tr>
     *     <tr>
     *         <td>data</td>
     *         <td>所有对象组成的列表</td>
     *         <td></td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
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
