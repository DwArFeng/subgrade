package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 逻辑或谓词子句测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class OrPredicateClauseTest {

    @Test
    public void shouldMatchNothingWithoutPredicates() {
        OrPredicateClause clause = new OrPredicateClause(List.of());

        assertEquals("(1=0)", clause.toHql());
        assertEquals(Map.of(), clause.toParamMap());
    }

    @Test
    public void shouldPreserveSinglePredicate() {
        OrPredicateClause clause = new OrPredicateClause(List.of(new EqPredicateClause("entity.name", "first")));

        assertEquals("(entity.name = :0_value)", clause.toHql());
        assertEquals(Map.of("0_value", "first"), clause.toParamMap());
    }

    @Test
    public void shouldKeepParametersDistinctInPredicateOrder() {
        OrPredicateClause clause = new OrPredicateClause(List.of(
                new EqPredicateClause("entity.name", "first"),
                new EqPredicateClause("entity.name", "second")
        ));

        assertEquals("(entity.name = :0_value OR entity.name = :1_value)", clause.toHql());
        assertEquals(Map.of("0_value", "first", "1_value", "second"), clause.toParamMap());
        assertEquals(List.of("0_value", "1_value"), List.copyOf(clause.toParamMap().keySet()));
        assertThrows(UnsupportedOperationException.class, () -> clause.toParamMap().put("extra", "value"));
    }
}
