package com.dwarfeng.subgrade.data.impl.handler;

import com.dwarfeng.subgrade.data.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseTask;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

/**
 * 使用 Jdbc 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class JdbcDatabaseHandler<T> implements DatabaseHandler<T> {

    private JdbcTemplate jdbcTemplate;

    public JdbcDatabaseHandler(@NotNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public T executeTask(@NotNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return jdbcTemplate.execute((ConnectionCallback<T>) con -> {
                try {
                    return databaseTask.todo(con);
                } catch (DataAccessException | SQLException e) {
                    throw e;
                } catch (Exception e) {
                    throw new SQLException(e);
                }
            });
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(@NotNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
