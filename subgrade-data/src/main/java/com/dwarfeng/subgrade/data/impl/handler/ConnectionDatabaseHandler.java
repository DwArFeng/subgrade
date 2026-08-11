package com.dwarfeng.subgrade.data.impl.handler;

import com.dwarfeng.subgrade.data.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseTask;

import org.jetbrains.annotations.NotNull;
import java.sql.Connection;

/**
 * 使用原生连接实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class ConnectionDatabaseHandler<T> implements DatabaseHandler<T> {

    private Connection connection;

    public ConnectionDatabaseHandler(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public T executeTask(@NotNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(connection);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(@NotNull Connection connection) {
        this.connection = connection;
    }
}
