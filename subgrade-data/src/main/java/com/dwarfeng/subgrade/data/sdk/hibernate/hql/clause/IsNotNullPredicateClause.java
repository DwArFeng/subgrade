package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Map;

/**
 * Is not null 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class IsNotNullPredicateClause extends AbstractPredicateClause {

    @NotNull
    private final String expression;

    public IsNotNullPredicateClause(@NotNull String expression) {
        this.expression = expression;
    }

    @Override
    protected String buildHql() {
        return String.format("%s IS NOT NULL", expression);
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        return Collections.emptyMap();
    }

    @NotNull
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "IsNotNullPredicateClause{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
