package com.dwarfeng.subgrade.sdk.database.executor;

import java.sql.Connection;

/**
 * 数据库任务。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface DatabaseTask<T> {

    /**
     * 执行数据库的任务。
     *
     * @param connection 数据库的原生连接。
     * @return 返回的值。
     * @throws Exception 任务执行过程中发生的任何异常。
     */
    T todo(Connection connection) throws Exception;
}
