package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;

/**
 * 使用 Hibernate 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class HibernateDatabaseHandler<T> implements DatabaseHandler<T> {

    private HibernateTemplate hibernateTemplate;

    public HibernateDatabaseHandler(@Nonnull HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public T executeTask(@Nonnull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
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

    public void setHibernateTemplate(@Nonnull HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
