package com.dwarfeng.subgrade.sdk.database.executor;

import org.hibernate.HibernateException;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

/**
 * 使用 Hibernate 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class HibernateDatabaseExecutor<T> implements DatabaseExecutor<T> {

    private HibernateTemplate hibernateTemplate;

    public HibernateDatabaseExecutor(@NonNull HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public T executeTask(@NonNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return hibernateTemplate.executeWithNativeSession(session -> session.doReturningWork(
                    connection -> {
                        try {
                            return databaseTask.todo(connection);
                        } catch (Exception e) {
                            throw new HibernateException(e);
                        }
                    }
            ));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(@NonNull HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
