package com.dwarfeng.subgrade.sdk.hibernate.nativesql;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

import java.sql.Connection;
import java.util.List;

/**
 * 本地 SQL 查询。
 *
 * @author DwArFeng
 * @since 1.2.8
 */
public interface NativeSqlLookup<PE extends Bean> {

    /**
     * 初始化本地 SQL 查询。
     *
     * <p>
     * 部分本地查询可以通过该方法验证数据表结构的完整性，如检查索引是否存在，关键字段是否存在等。
     *
     * <p>
     * 如果方法在执行过程中发生了任何自定义的异常（如参数错误等），请抛出 {@link RuntimeException}。
     *
     * @param connection JDBC连接。
     * @throws RuntimeException 方法执行过程中发生的任何自定义异常。
     */
    void init(Connection connection) throws RuntimeException;

    /**
     * 查询实体。
     *
     * <p>
     * 当查询正常时，返回列表；当查询遇到了任何异常，返回 {@link RuntimeException}。
     *
     * <p>
     * 如果方法在执行过程中发生了任何自定义的异常（如参数错误等），请抛出 {@link RuntimeException}。
     *
     * @param connection 数据库连接。
     * @param preset     预设名称。
     * @param args       参数。
     * @return 实体组成的列表，或者是 null。
     * @throws RuntimeException 方法执行过程中发生的任何自定义异常。
     */
    List<PE> lookupEntity(Connection connection, String preset, Object[] args) throws RuntimeException;

    /**
     * 查询实体。
     *
     * <p>
     * 当查询正常时，返回列表；当查询遇到了任何异常，返回 {@link RuntimeException}。
     *
     * <p>
     * 如果方法在执行过程中发生了任何自定义的异常（如参数错误等），请抛出 {@link RuntimeException}。
     *
     * @param connection 数据库连接。
     * @param preset     预设名称。
     * @param args       参数。
     * @param pagingInfo 分页信息。
     * @return 实体组成的列表，或者是 null。
     * @throws RuntimeException 方法执行过程中发生的任何自定义异常。
     */
    List<PE> lookupEntity(Connection connection, String preset, Object[] args, PagingInfo pagingInfo)
            throws RuntimeException;

    /**
     * 查询实体数量。
     *
     * <p>
     * 当查询正常时，返回数量值；当查询遇到了任何异常，返回 {@link RuntimeException}。
     *
     * <p>
     * 如果方法在执行过程中发生了任何自定义的异常（如参数错误等），请抛出 {@link RuntimeException}。
     *
     * @param connection 数据库连接。
     * @param preset     预设名称。
     * @param args       参数。
     * @return 实体的数量值，或者是 null。
     * @throws RuntimeException 方法执行过程中发生的任何自定义异常。
     */
    int lookupCount(Connection connection, String preset, Object[] args) throws RuntimeException;
}
