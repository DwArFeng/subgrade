package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.PredicateClause;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 逻辑非谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class NotPredicateClause extends AbstractPredicateClause {

    @Nonnull
    private final PredicateClause predicateClause;

    public NotPredicateClause(@Nonnull PredicateClause predicateClause) {
        this.predicateClause = predicateClause;
    }

    @Override
    protected String buildHql() {
        StringBuilder hqlBuilder = new StringBuilder();

        // 拼接左括号。
        hqlBuilder.append("(");

        // 展开参数。
        String hql = predicateClause.toHql();
        hqlBuilder.append("NOT (");
        hqlBuilder.append(hql);
        hqlBuilder.append(")");

        // 拼接右括号。
        hqlBuilder.append(")");

        return hqlBuilder.toString();
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        return predicateClause.toParamMap();
    }

    @Override
    public String toString() {
        return "NotPredicateClause{" +
                "predicateClause=" + predicateClause +
                '}';
    }
}
