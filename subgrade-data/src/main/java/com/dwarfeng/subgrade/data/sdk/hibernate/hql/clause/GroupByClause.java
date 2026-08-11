package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

/**
 * Group by 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class GroupByClause extends AbstractClause {

    @NotNull
    private final String expression;

    public GroupByClause(@NotNull String expression) {
        this.expression = expression;
    }

    @Override
    protected String buildHql() {
        return expression;
    }

    @NotNull
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
