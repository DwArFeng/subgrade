package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 存在谓词子句测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ExistsPredicateClauseTest {

    @Test
    public void shouldBuildCorrelatedSubqueryWithoutParameters() {
        PredicateClause predicate = new EqExpressionPredicateClause("child.parentId", "parent.id");
        ExistsPredicateClause clause = new ExistsPredicateClause(Object.class, "child", predicate);

        assertEquals("EXISTS (SELECT child FROM java.lang.Object AS child WHERE child.parentId = parent.id)",
                clause.toHql());
        assertEquals(Map.of(), clause.toParamMap());
        assertSame(Object.class, clause.getEntityClass());
        assertEquals("child", clause.getEntityAlias());
        assertSame(predicate, clause.getPredicateClause());
    }

    @Test
    public void shouldCopyParametersIntoUnmodifiableMap() {
        PredicateClause predicate = new EqPredicateClause("child.status", "active");
        ExistsPredicateClause clause = new ExistsPredicateClause(Object.class, "child", predicate);

        assertEquals("EXISTS (SELECT child FROM java.lang.Object AS child WHERE child.status = :value)",
                clause.toHql());
        assertEquals(Map.of("value", "active"), clause.toParamMap());
        assertNotSame(predicate.toParamMap(), clause.toParamMap());
        assertThrows(UnsupportedOperationException.class, () -> clause.toParamMap().put("extra", "value"));
    }

    @Test
    public void shouldReassignNestedParametersAlongsideOuterParameters() {
        PredicateClause predicate = new AndPredicateClause(List.of(
                new EqExpressionPredicateClause("child.parentId", "parent.id"),
                new OrPredicateClause(List.of(
                        new EqPredicateClause("child.status", "active"),
                        new EqPredicateClause("child.status", "pending")
                ))
        ));
        ExistsPredicateClause clause = new ExistsPredicateClause(Object.class, "child", predicate);
        HqlCondition condition = new HqlCondition(Object.class, "parent")
                .setQueryType(QueryType.ENTITY)
                .addWhereClause(new EqPredicateClause("parent.name", "sample"))
                .addWhereClause(clause);

        HqlQueryInfo queryInfo = HqlQueryInfoFactory.buildQueryInfoFormCondition(condition);

        assertEquals("SELECT parent FROM java.lang.Object AS parent WHERE parent.name = :var_0 AND " +
                        "EXISTS (SELECT child FROM java.lang.Object AS child WHERE " +
                        "(child.parentId = parent.id AND (child.status = :var_1 OR child.status = :var_2)))",
                queryInfo.getHql());
        assertEquals(Map.of("var_0", "sample", "var_1", "active", "var_2", "pending"), queryInfo.getParamMap());
        assertEquals(Map.of("1_0_value", "active", "1_1_value", "pending"), clause.toParamMap());
    }
}
