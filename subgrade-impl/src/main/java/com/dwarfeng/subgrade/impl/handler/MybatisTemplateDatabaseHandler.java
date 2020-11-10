package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Nonnull;

/**
 * 使用 Mybatis 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class MybatisTemplateDatabaseHandler<T> implements DatabaseHandler<T> {

    private SqlSessionTemplate sqlSessionTemplate;

    public MybatisTemplateDatabaseHandler(@Nonnull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public T executeTask(@Nonnull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(sqlSessionTemplate.getConnection());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(@Nonnull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
