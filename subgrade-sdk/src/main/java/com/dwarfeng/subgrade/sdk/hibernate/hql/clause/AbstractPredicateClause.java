package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.PredicateClause;

import java.util.Map;
import java.util.Objects;

/**
 * 谓词子句的抽象实现。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public abstract class AbstractPredicateClause implements PredicateClause {

    private String cachedHql = null;
    private Map<String, Object> cachedParamMap = null;

    @Override
    public String toHql() {
        if (Objects.isNull(cachedHql)) {
            cachedHql = buildHql();
        }
        return cachedHql;
    }

    /**
     * 构建 HQL。
     *
     * @return HQL。
     */
    protected abstract String buildHql();

    @Override
    public Map<String, Object> toParamMap() {
        if (Objects.isNull(cachedParamMap)) {
            cachedParamMap = buildParamMap();
        }
        return cachedParamMap;
    }

    /**
     * 构建参数映射。
     *
     * @return 参数映射。
     */
    protected abstract Map<String, Object> buildParamMap();

    @Override
    public String toString() {
        return "AbstractPredicateClause{}";
    }
}
