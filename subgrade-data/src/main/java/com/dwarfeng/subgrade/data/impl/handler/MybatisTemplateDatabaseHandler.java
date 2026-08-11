package com.dwarfeng.subgrade.data.impl.handler;

import com.dwarfeng.subgrade.data.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseTask;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 使用 Mybatis 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class MybatisTemplateDatabaseHandler<T> implements DatabaseHandler<T> {

    private SqlSessionTemplate sqlSessionTemplate;

    public MybatisTemplateDatabaseHandler(@NotNull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public T executeTask(@NotNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(sqlSessionTemplate.getConnection());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(@NotNull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
