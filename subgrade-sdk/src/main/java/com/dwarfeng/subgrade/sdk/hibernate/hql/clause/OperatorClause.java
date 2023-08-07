package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 操作符谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public abstract class OperatorClause extends AbstractPredicateClause {

    private static final String VARIABLE_NAME = "value";

    @Nonnull
    protected final String expression;
    @Nonnull
    protected final Object value;
    @Nonnull
    protected final String operator;

    public OperatorClause(@Nonnull String expression, @Nonnull Object value, @Nonnull String operator) {
        this.expression = expression;
        this.value = value;
        this.operator = operator;
    }

    @Override
    protected String buildHql() {
        return String.format(
                "%s %s :%s",
                expression,
                operator,
                VARIABLE_NAME
        );
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put(VARIABLE_NAME, value);
        return Collections.unmodifiableMap(paramMap);
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public Object getValue() {
        return value;
    }

    @Nonnull
    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "OperatorClause{" +
                "expression='" + expression + '\'' +
                ", value=" + value +
                ", operator='" + operator + '\'' +
                '}';
    }
}
