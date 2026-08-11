package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    private final String expression;
    @NotNull
    private final Collection<?> values;

    public InPredicateClause(@NotNull String expression, @NotNull Collection<?> values) {
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

    @NotNull
    public String getExpression() {
        return expression;
    }

    @NotNull
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
