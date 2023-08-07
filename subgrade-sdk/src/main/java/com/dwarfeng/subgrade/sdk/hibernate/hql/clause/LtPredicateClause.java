package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;

/**
 * 小于谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class LtPredicateClause extends OperatorClause {

    private static final String OPERATOR = "<";

    public LtPredicateClause(@Nonnull String expression, @Nonnull Object value) {
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
