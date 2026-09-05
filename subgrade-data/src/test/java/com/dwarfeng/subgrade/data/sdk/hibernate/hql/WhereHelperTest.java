package com.dwarfeng.subgrade.data.sdk.hibernate.hql;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.EqExpressionPredicateClause;
import com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.ExistsPredicateClause;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Where 子句帮助类测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class WhereHelperTest {

    @Test
    public void shouldCreateExpressionComparison() {
        PredicateClause clause = WhereHelper.eqExpression("child.parentId", "parent.id");

        assertInstanceOf(EqExpressionPredicateClause.class, clause);
        assertEquals("child.parentId = parent.id", clause.toHql());
        assertEquals(Map.of(), clause.toParamMap());
    }

    @Test
    public void shouldCreateExistsWithOriginalPredicate() {
        PredicateClause predicate = WhereHelper.eq("child.status", "active");
        PredicateClause clause = WhereHelper.exists(Object.class, "child", predicate);

        ExistsPredicateClause exists = assertInstanceOf(ExistsPredicateClause.class, clause);
        assertSame(predicate, exists.getPredicateClause());
        assertEquals("EXISTS (SELECT child FROM java.lang.Object AS child WHERE child.status = :value)",
                clause.toHql());
        assertEquals(Map.of("value", "active"), clause.toParamMap());
    }

    @Test
    public void shouldComposeCorrelatedExistsWithOtherHelpers() {
        PredicateClause clause = WhereHelper.exists(Object.class, "child", WhereHelper.and(
                WhereHelper.eqExpression("child.parentId", "parent.id"),
                WhereHelper.eq("child.status", "active")
        ));

        assertEquals("EXISTS (SELECT child FROM java.lang.Object AS child WHERE " +
                "(child.parentId = parent.id AND child.status = :1_value))", clause.toHql());
        assertEquals(Map.of("1_value", "active"), clause.toParamMap());
    }
}
