package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    protected final String expression;
    @NotNull
    protected final Object value;
    @NotNull
    protected final String operator;

    public OperatorClause(@NotNull String expression, @NotNull Object value, @NotNull String operator) {
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

    @NotNull
    public String getExpression() {
        return expression;
    }

    @NotNull
    public Object getValue() {
        return value;
    }

    @NotNull
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
