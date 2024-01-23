package com.dwarfeng.subgrade.sdk.hibernate.hql;

/**
 * 无排序的 HqlCondition。
 *
 * <p>
 * 该类的 {@link #addOrderByClause(Clause)} 方法不会添加任何排序，可以用于 count 等不需要排序的查询。
 *
 * @author DwArFeng
 * @since 1.4.8
 */
public class NoOrderHqlCondition extends HqlCondition {

    public NoOrderHqlCondition() {
        super();
    }

    public NoOrderHqlCondition(Class<?> entityClass) {
        super(entityClass);
    }

    public NoOrderHqlCondition(Class<?> entityClass, String entityAlias) {
        super(entityClass, entityAlias);
    }

    @Override
    public HqlCondition addOrderByClause(Clause orderByClause) {
        // Do nothing.
        return this;
    }
}
