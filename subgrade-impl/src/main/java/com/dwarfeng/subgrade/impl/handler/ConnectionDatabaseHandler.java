package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;

import javax.annotation.Nonnull;
import java.sql.Connection;

/**
 * 使用原生连接实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class ConnectionDatabaseHandler<T> implements DatabaseHandler<T> {

    private Connection connection;

    public ConnectionDatabaseHandler(@Nonnull Connection connection) {
        this.connection = connection;
    }

    @Override
    public T executeTask(@Nonnull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(connection);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(@Nonnull Connection connection) {
        this.connection = connection;
    }
}
