package com.dwarfeng.subgrade.data.sdk.hibernate.hql;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.JoinClause;

import org.jetbrains.annotations.NotNull;

/**
 * Join 子句帮助类。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public final class JoinHelper {

    /**
     * 生成 Join 子句。
     *
     * @param joinType      连接类型。
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause join(
            @NotNull JoinType joinType,
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return new JoinClause(joinType, entityClass, alias, joinCondition);
    }

    /**
     * 生成内连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause innerJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.INNER, entityClass, alias, joinCondition);
    }

    /**
     * 生成左连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause leftJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.LEFT, entityClass, alias, joinCondition);
    }

    /**
     * 生成左外连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause leftOuterJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.LEFT_OUTER, entityClass, alias, joinCondition);
    }

    /**
     * 生成右连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause rightJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.RIGHT, entityClass, alias, joinCondition);
    }

    /**
     * 生成右外连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause rightOuterJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.RIGHT_OUTER, entityClass, alias, joinCondition);
    }

    /**
     * 生成全连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause fullJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.FULL, entityClass, alias, joinCondition);
    }

    /**
     * 生成全外连接 Join 子句。
     *
     * @param entityClass   连接的实体类。
     * @param alias         连接的别名。
     * @param joinCondition 连接条件。
     * @return 生成的 Join 子句。
     */
    public static JoinClause fullOuterJoin(
            @NotNull Class<?> entityClass,
            @NotNull String alias,
            @NotNull String joinCondition
    ) {
        return join(JoinType.FULL_OUTER, entityClass, alias, joinCondition);
    }

    private JoinHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
