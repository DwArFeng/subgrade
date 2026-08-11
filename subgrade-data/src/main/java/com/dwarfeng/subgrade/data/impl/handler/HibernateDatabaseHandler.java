package com.dwarfeng.subgrade.data.impl.handler;

import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.exception.DatabaseException;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.data.stack.handler.DatabaseTask;
import org.hibernate.HibernateException;

import org.jetbrains.annotations.NotNull;

/**
 * 使用 Hibernate 实现的数据库执行器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public class HibernateDatabaseHandler<T> implements DatabaseHandler<T> {

    private HibernateOperations hibernateOperations;

    public HibernateDatabaseHandler(@NotNull HibernateOperations hibernateOperations) {
        this.hibernateOperations = hibernateOperations;
    }

    @Override
    public T executeTask(@NotNull DatabaseTask<? extends T> databaseTask) throws DatabaseException {
        try {
            return hibernateOperations.executeWithNativeSession(session -> session.doReturningWork(
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

    public HibernateOperations getHibernateOperations() {
        return hibernateOperations;
    }

    public void setHibernateOperations(@NotNull HibernateOperations hibernateOperations) {
        this.hibernateOperations = hibernateOperations;
    }
}
