package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

/**
 * 大于谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class GtPredicateClause extends OperatorClause {

    private static final String OPERATOR = ">";

    public GtPredicateClause(@NotNull String expression, @NotNull Object value) {
        super(expression, value, OPERATOR);
    }

    @Override
    public String toString() {
        return "GtPredicateClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                '}';
    }
}
