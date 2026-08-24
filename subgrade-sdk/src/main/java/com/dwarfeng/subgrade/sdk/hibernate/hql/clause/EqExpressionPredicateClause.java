package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

/**
 * 表达式等于谓词子句。
 *
 * <p>
 * 该谓词用于比较两个受信任的 HQL 表达式，不会为表达式生成查询参数。
 *
 * @author DwArFeng
 * @since 1.8.5
 */
public class EqExpressionPredicateClause extends AbstractPredicateClause {

    @Nonnull
    private final String leftExpression;
    @Nonnull
    private final String rightExpression;

    public EqExpressionPredicateClause(@Nonnull String leftExpression, @Nonnull String rightExpression) {
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

    @Nonnull
    public String getLeftExpression() {
        return leftExpression;
    }

    @Nonnull
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
