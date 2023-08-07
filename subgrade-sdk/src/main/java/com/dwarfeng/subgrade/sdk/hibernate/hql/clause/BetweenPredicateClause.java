package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Between 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class BetweenPredicateClause extends AbstractPredicateClause {

    private static final String VARIABLE_NAME_1 = "value1";
    private static final String VARIABLE_NAME_2 = "value2";

    @Nonnull
    private final String expression;
    @Nonnull
    private final Object value1;
    @Nonnull
    private final Object value2;

    public BetweenPredicateClause(@Nonnull String expression, @Nonnull Object value1, @Nonnull Object value2) {
        this.expression = expression;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    protected String buildHql() {
        return String.format("%s BETWEEN :%s AND :%s", expression, VARIABLE_NAME_1, VARIABLE_NAME_2);
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put(VARIABLE_NAME_1, value1);
        paramMap.put(VARIABLE_NAME_2, value2);
        return Collections.unmodifiableMap(paramMap);
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public Object getValue1() {
        return value1;
    }

    @Nonnull
    public Object getValue2() {
        return value2;
    }

    @Override
    public String toString() {
        return "BetweenPredicateClause{" +
                "expression='" + expression + '\'' +
                ", value1=" + value1 +
                ", value2=" + value2 +
                '}';
    }
}
