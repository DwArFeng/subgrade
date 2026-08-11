package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

/**
 * 小于等于谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class LePredicateClause extends OperatorClause {

    private static final String OPERATOR = "<=";

    public LePredicateClause(@NotNull String expression, @NotNull Object value) {
        super(expression, value, OPERATOR);
    }

    @Override
    public String toString() {
        return "LePredicateClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                '}';
    }
}
