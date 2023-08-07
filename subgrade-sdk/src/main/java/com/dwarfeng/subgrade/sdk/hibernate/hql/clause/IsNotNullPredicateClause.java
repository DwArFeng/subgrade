package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

/**
 * Is not null 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class IsNotNullPredicateClause extends AbstractPredicateClause {

    @Nonnull
    private final String expression;

    public IsNotNullPredicateClause(@Nonnull String expression) {
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

    @Nonnull
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
