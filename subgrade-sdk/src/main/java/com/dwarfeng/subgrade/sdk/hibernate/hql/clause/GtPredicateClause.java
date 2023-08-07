package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;

/**
 * 大于谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class GtPredicateClause extends OperatorClause {

    private static final String OPERATOR = ">";

    public GtPredicateClause(@Nonnull String expression, @Nonnull Object value) {
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
