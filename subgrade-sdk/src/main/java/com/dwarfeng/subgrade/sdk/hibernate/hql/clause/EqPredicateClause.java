package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;

/**
 * 等于子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class EqPredicateClause extends OperatorClause {

    private static final String OPERATOR = "=";

    public EqPredicateClause(@Nonnull String expression, @Nonnull Object value) {
        super(expression, value, OPERATOR);
    }

    @Override
    public String toString() {
        return "EqPredicateClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                '}';
    }
}
