package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.subgrade.sdk.hibernate.hql.clause.GroupByClause;

/**
 * Group by 子句帮助类。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public final class GroupByHelper {

    /**
     * 生成一个 Group by 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Group by 子句。
     */
    public static GroupByClause groupBy(String expression) {
        return new GroupByClause(expression);
    }

    private GroupByHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
