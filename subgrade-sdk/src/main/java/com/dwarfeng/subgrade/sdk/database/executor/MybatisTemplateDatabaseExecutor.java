package com.dwarfeng.subgrade.sdk.database.executor;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.NonNull;

/**
 * 使用 Mybatis 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisTemplateDatabaseExecutor<T> implements DatabaseExecutor<T> {

    private SqlSessionTemplate sqlSessionTemplate;

    public MybatisTemplateDatabaseExecutor(@NonNull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public T executeTask(@NonNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return databaseTask.todo(sqlSessionTemplate.getConnection());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(@NonNull SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
