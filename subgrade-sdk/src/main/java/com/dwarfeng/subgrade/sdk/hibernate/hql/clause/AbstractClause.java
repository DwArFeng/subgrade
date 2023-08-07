package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.Clause;

import java.util.Objects;

/**
 * 子句的抽象实现。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public abstract class AbstractClause implements Clause {

    private String cachedHql = null;

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
    public String toString() {
        return "AbstractClause{}";
    }
}
