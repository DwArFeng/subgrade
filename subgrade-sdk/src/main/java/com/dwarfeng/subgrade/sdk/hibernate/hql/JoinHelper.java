package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.subgrade.sdk.hibernate.hql.clause.JoinClause;

import javax.annotation.Nonnull;

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
            @Nonnull JoinType joinType,
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
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
            @Nonnull Class<?> entityClass,
            @Nonnull String alias,
            @Nonnull String joinCondition
    ) {
        return join(JoinType.FULL_OUTER, entityClass, alias, joinCondition);
    }

    private JoinHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
