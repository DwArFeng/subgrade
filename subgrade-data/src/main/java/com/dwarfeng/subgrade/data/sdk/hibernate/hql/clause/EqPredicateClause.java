package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

/**
 * 等于子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class EqPredicateClause extends OperatorClause {

    private static final String OPERATOR = "=";

    public EqPredicateClause(@NotNull String expression, @NotNull Object value) {
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
