package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 全体查询服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractEntireLookupService<E extends Entity<?>> implements EntireLookupService<E> {

    @Nonnull
    protected ServiceExceptionMapper sem;

    @Nonnull
    protected LogLevel exceptionLogLevel;

    public AbstractEntireLookupService(@Nonnull ServiceExceptionMapper sem, @Nonnull LogLevel exceptionLogLevel) {
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public PagedData<E> lookup() throws ServiceException {
        try {
            return doLookup();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询所有实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询所有实体。
     *
     * @return 带有分页信息的所有实体。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookup()
     */
    protected abstract PagedData<E> doLookup() throws Exception;

    @Override
    public List<E> lookupAsList() throws ServiceException {
        try {
            return doLookupAsList();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询所有实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询所有实体。
     *
     * <p>
     * 查询所有实体，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup()} 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup()} 相同，需要实现类重写，以满足到提高效率的需求。
     *
     * @return 实体组成的列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookupAsList()
     */
    protected abstract List<E> doLookupAsList() throws Exception;

    @Override
    public PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException {
        try {
            return doLookup(pagingInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询分页实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询分页实体。
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
     *         <td>所有实体组成的列表或空列表</td>
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
     *         <td>所有实体组成的列表</td>
     *         <td></td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
     *
     * @param pagingInfo 实体的分页信息。
     * @return 带有分页信息的指定页上的实体。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookup(PagingInfo)
     */
    protected abstract PagedData<E> doLookup(PagingInfo pagingInfo) throws Exception;

    @Override
    public List<E> lookupAsList(PagingInfo pagingInfo) throws ServiceException {
        try {
            return doLookupAsList(pagingInfo);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询分页实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询分页实体。
     *
     * <p>
     * 查询分页实体，并以列表的形式返回。
     *
     * <p>
     * 在该接口的大量实践中发现，当数据量过大时，使用 {@link #lookup(PagingInfo)}
     * 方法查询会消耗过多的时间查询其中的数据总量，
     * 在部分应用场景下，数据总量是没有必要的。该方法可直接返回实体列表，可以跳过总量的查询，
     * 增加特性应用场景下的查询效率。<br>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(PagingInfo)} 相同，需要实现类重写，以满足到提高效率的需求。
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
     *         <td>所有实体组成的列表或空列表</td>
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
     *         <td>所有实体组成的列表</td>
     *         <td></td>
     *     </tr>
     * </table>
     * 该特性默认启用，可以通过设置系统参数 <code>-Dsubgrade.useStrictPaging=true</code> 来关闭。
     * 关闭后，使用每页行数小于 0 的分页信息将会抛出异常。<br>
     * 在该特性开启的情况下，当每页行数小于 0 时，查询全部数据，并向日志中记录警告信息。
     * 记录警告日志的行为默认启用，可以通过设置系统参数 <code>-Dsubgrade.logPagingWarning=false</code> 来关闭。<br>
     * 所有系统参数的值均可以在 sdk 模块的 <code>com.dwarfeng.subgrade.sdk.SystemPropertyConstants</code> 类中找到。
     *
     * @param pagingInfo 分页信息。
     * @return 实体组成的列表。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookupAsList(PagingInfo)
     */
    protected abstract List<E> doLookupAsList(PagingInfo pagingInfo) throws Exception;

    @Override
    public E lookupFirst() throws ServiceException {
        try {
            return doLookupFirst();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询首个实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询首个实体。
     *
     * <p>
     * 当数据访问层中存在数据时，返回第一个数据；当数据访问层中不存在数据时，返回 null。
     *
     * @return 数据访问层中的第一个实体，或者是 null。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookupFirst()
     */
    protected abstract E doLookupFirst() throws Exception;

    @Override
    public int lookupCount() throws ServiceException {
        try {
            return doLookupCount();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("查询实体数量时发生异常", exceptionLogLevel, e, sem);
        }
    }

    /**
     * 查询实体数量。
     *
     * <p>
     * 在该接口的大量实践中发现，许多场景只需要获取实体的数量，而不需要获取实体列表。
     * 使用 {@link #lookup()} 或 {@link #lookup(PagingInfo)} 方法查询会造成不必要的性能浪费。<br>
     * 该方法可以直接返回数据总量，在部分场景下使用该方法可以提高查询的效率。
     *
     * <p>
     * 该接口方法拥有默认实现，其效率与 {@link #lookup(PagingInfo)} 相同，需要实现类重写，以满足到提高效率的需求。
     *
     * @return 实体的数量。
     * @throws Exception 方法执行时发生的任何异常。
     * @see #lookupCount()
     */
    protected abstract int doLookupCount() throws Exception;

    @Nonnull
    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@Nonnull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    @Nonnull
    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@Nonnull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public String toString() {
        return "AbstractEntireLookupService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
