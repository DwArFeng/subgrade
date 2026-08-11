package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.OrderType;

import org.jetbrains.annotations.NotNull;

/**
 * Order by 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class OrderByClause extends AbstractClause {

    @NotNull
    private final String expression;
    @NotNull
    private final OrderType orderType;

    public OrderByClause(@NotNull String expression, @NotNull OrderType orderType) {
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
        return switch (orderType) {
            case ASC -> "ASC";
            case DESC -> "DESC";
        };
    }

    @NotNull
    public String getExpression() {
        return expression;
    }

    @NotNull
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
