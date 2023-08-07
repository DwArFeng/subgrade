package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * In 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class InPredicateClause extends AbstractPredicateClause {

    private static final String VARIABLE_NAME = "values";

    @Nonnull
    private final String expression;
    @Nonnull
    private final Collection<?> values;

    public InPredicateClause(@Nonnull String expression, @Nonnull Collection<?> values) {
        this.expression = expression;
        this.values = values;
    }

    @Override
    protected String buildHql() {
        return String.format("%s IN :%s", expression, VARIABLE_NAME);
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put(VARIABLE_NAME, values);
        return Collections.unmodifiableMap(paramMap);
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public Collection<?> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "InPredicateClause{" +
                "expression='" + expression + '\'' +
                ", values=" + values +
                '}';
    }
}
