package com.dwarfeng.subgrade.sdk.database.executor;

import org.springframework.lang.NonNull;

import java.sql.Connection;

/**
 * 使用原生连接实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class ConnectionDatabaseExecutor<T> implements DatabaseExecutor<T> {

    private Connection connection;

    public ConnectionDatabaseExecutor(@NonNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public T executeTask(@NonNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(connection);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(@NonNull Connection connection) {
        this.connection = connection;
    }
}
