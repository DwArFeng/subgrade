package com.dwarfeng.subgrade.sdk.database.executor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.sql.SQLException;

/**
 * 使用 Jdbc 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class JdbcDatabaseExecutor<T> implements DatabaseExecutor<T> {

    private JdbcTemplate jdbcTemplate;

    public JdbcDatabaseExecutor(@NonNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public T executeTask(@NonNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
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

    public void setJdbcTemplate(@NonNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
