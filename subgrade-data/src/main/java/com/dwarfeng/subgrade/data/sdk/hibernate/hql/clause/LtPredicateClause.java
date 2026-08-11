package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

/**
 * 小于谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class LtPredicateClause extends OperatorClause {

    private static final String OPERATOR = "<";

    public LtPredicateClause(@NotNull String expression, @NotNull Object value) {
        super(expression, value, OPERATOR);
    }

    @Override
    public String toString() {
        return "LtPredicateClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                '}';
    }
}
