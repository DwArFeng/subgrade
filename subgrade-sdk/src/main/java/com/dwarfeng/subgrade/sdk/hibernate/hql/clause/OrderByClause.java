package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.OrderType;

import javax.annotation.Nonnull;

/**
 * Order by 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class OrderByClause extends AbstractClause {

    @Nonnull
    private final String expression;
    @Nonnull
    private final OrderType orderType;

    public OrderByClause(@Nonnull String expression, @Nonnull OrderType orderType) {
        this.expression = expression;
        this.orderType = orderType;
    }

    @Override
    protected String buildHql() {
        return String.format(
                "%s %s",
                expression,
                orderTypeToHql()
        );
    }

    private String orderTypeToHql() {
        switch (orderType) {
            case ASC:
                return "ASC";
            case DESC:
                return "DESC";
            default:
                throw new IllegalArgumentException("非法的枚举值: " + orderType);
        }
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public String toString() {
        return "OrderByClause{" +
                "expression='" + expression + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
