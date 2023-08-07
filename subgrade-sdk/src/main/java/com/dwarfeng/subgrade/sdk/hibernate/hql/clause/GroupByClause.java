package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;

/**
 * Group by 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class GroupByClause extends AbstractClause {

    @Nonnull
    private final String expression;

    public GroupByClause(@Nonnull String expression) {
        this.expression = expression;
    }

    @Override
    protected String buildHql() {
        return expression;
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "GroupByClause{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
