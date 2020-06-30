package com.dwarfeng.subgrade.sdk.database.executor;

import org.springframework.lang.NonNull;

/**
 * 数据库执行器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface DatabaseExecutor<T> {

    /**
     * 执行数据库任务。
     *
     * @param databaseTask 指定的数据库任务。
     * @return 数据库任务执行返回的值。
     * @throws DatabaseException 数据库异常。
     */
    T executeTask(@NonNull DatabaseTask<? extends T> databaseTask) throws DatabaseException;
}
