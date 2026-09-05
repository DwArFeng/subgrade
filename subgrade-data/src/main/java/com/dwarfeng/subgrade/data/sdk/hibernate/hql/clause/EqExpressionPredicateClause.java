package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * 表达式等于谓词子句。
 *
 * <p>
 * 该谓词用于比较两个受信任的 HQL 表达式，不会为表达式生成查询参数。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class EqExpressionPredicateClause extends AbstractPredicateClause {

    @NotNull
    private final String leftExpression;
    @NotNull
    private final String rightExpression;

    public EqExpressionPredicateClause(@NotNull String leftExpression, @NotNull String rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    protected String buildHql() {
        return leftExpression + " = " + rightExpression;
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        return Collections.emptyMap();
    }

    @NotNull
    public String getLeftExpression() {
        return leftExpression;
    }

    @NotNull
    public String getRightExpression() {
        return rightExpression;
    }

    @Override
    public String toString() {
        return "EqExpressionPredicateClause{" +
                "leftExpression='" + leftExpression + '\'' +
                ", rightExpression='" + rightExpression + '\'' +
                '}';
    }
}
