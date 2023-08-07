package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.subgrade.sdk.hibernate.hql.clause.OrderByClause;

import javax.annotation.Nonnull;

/**
 * Order by 子句帮助类。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public final class OrderByHelper {

    /**
     * 生成 Order by 子句。
     *
     * @param expression 属性表达式。
     * @param orderType  排序类型。
     * @return 生成的 Order by 子句。
     */
    public static OrderByClause orderBy(@Nonnull String expression, @Nonnull OrderType orderType) {
        return new OrderByClause(expression, orderType);
    }

    /**
     * 生成升序 Order by 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Order by 子句。
     */
    public static OrderByClause asc(@Nonnull String expression) {
        return orderBy(expression, OrderType.ASC);
    }

    /**
     * 生成降序 Order by 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Order by 子句。
     */
    public static OrderByClause desc(@Nonnull String expression) {
        return orderBy(expression, OrderType.DESC);
    }

    private OrderByHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
