package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 表达式等于谓词子句测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class EqExpressionPredicateClauseTest {

    @Test
    public void shouldCompareExpressionsWithoutBindingParameters() {
        EqExpressionPredicateClause clause = new EqExpressionPredicateClause("child.parentId", "parent.id");

        assertEquals("child.parentId = parent.id", clause.toHql());
        assertEquals(Map.of(), clause.toParamMap());
        assertEquals("child.parentId", clause.getLeftExpression());
        assertEquals("parent.id", clause.getRightExpression());
    }
}
