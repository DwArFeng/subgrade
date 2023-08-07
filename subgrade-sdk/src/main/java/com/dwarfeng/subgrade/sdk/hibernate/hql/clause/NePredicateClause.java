package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

/**
 * 不等于子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class NePredicateClause extends OperatorClause {

    private static final String OPERATOR = "<>";

    public NePredicateClause(String expression, Object value) {
        super(expression, value, OPERATOR);
    }

    @Override
    public String toString() {
        return "NePredicateClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                '}';
    }
}
