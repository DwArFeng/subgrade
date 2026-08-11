package com.dwarfeng.subgrade.data.sdk.hibernate.operation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Hibernate 会话操作器。
 *
 * <p>
 * 该类为 Subgrade 的 Hibernate DAO 提供基于当前事务会话的最小操作集合。
 * 调用方应通过 Spring 或其它事务管理器将 {@link SessionFactory#getCurrentSession()} 绑定到当前执行上下文。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class HibernateOperations {

    @NotNull
    private SessionFactory sessionFactory;

    public HibernateOperations(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 使用当前 Hibernate 会话执行操作。
     *
     * @param action 指定的操作。
     * @param <T>    操作结果类型。
     * @return 操作结果。
     */
    public <T> T execute(@NotNull Function<Session, T> action) {
        return action.apply(currentSession());
    }

    /**
     * 使用当前原生 Hibernate 会话执行操作。
     *
     * @param action 指定的操作。
     * @param <T>    操作结果类型。
     * @return 操作结果。
     */
    public <T> T executeWithNativeSession(@NotNull Function<Session, T> action) {
        return execute(action);
    }

    /**
     * 持久化实体并返回其标识。
     *
     * @param entity 指定的实体。
     * @return 实体标识。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public Object save(@NotNull Object entity) {
        Session session = currentSession();
        session.persist(entity);
        return session.getIdentifier(entity);
    }

    /**
     * 合并实体状态。
     *
     * @param entity 指定的实体。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public void update(@NotNull Object entity) {
        currentSession().merge(entity);
    }

    /**
     * 持久化或合并实体状态。
     *
     * @param entity 指定的实体。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public void saveOrUpdate(@NotNull Object entity) {
        Session session = currentSession();
        if (session.contains(entity)) {
            return;
        }
        session.merge(entity);
    }

    /**
     * 删除实体。
     *
     * @param entity 指定的实体。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public void delete(@NotNull Object entity) {
        Session session = currentSession();
        Object managedEntity = session.contains(entity) ? entity : session.merge(entity);
        session.remove(managedEntity);
    }

    /**
     * 按标识查询实体。
     *
     * @param entityClass 指定的实体类型。
     * @param identifier  指定的实体标识。
     * @param <T>         实体类型。
     * @return 查询结果。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public <T> T get(@NotNull Class<T> entityClass, Object identifier) {
        return currentSession().find(entityClass, identifier);
    }

    /**
     * 将当前会话中的变更同步至数据库。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public void flush() {
        currentSession().flush();
    }

    /**
     * 清除当前会话中的持久化上下文。
     */
    // 当前会话由事务上下文管理生命周期，本方法不会主动关闭该会话。
    @SuppressWarnings("resource")
    public void clear() {
        currentSession().clear();
    }

    @NotNull
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public String toString() {
        return "HibernateOperations{" +
                "sessionFactory=" + sessionFactory +
                '}';
    }
}
