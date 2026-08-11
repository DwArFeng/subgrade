package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.JoinType;

import org.jetbrains.annotations.NotNull;

/**
 * Join 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class JoinClause extends AbstractClause {

    @NotNull
    private final JoinType joinType;
    @NotNull
    private final Class<?> joinClass;
    @NotNull
    private final String joinAlias;
    @NotNull
    private final String joinCondition;

    public JoinClause(
            @NotNull JoinType joinType,
            @NotNull Class<?> joinClass,
            @NotNull String joinAlias,
            @NotNull String joinCondition
    ) {
        this.joinType = joinType;
        this.joinClass = joinClass;
        this.joinAlias = joinAlias;
        this.joinCondition = joinCondition;
    }

    @Override
    protected String buildHql() {
        return String.format(
                "%s %s AS %s ON %s",
                joinTypeToHql(),
                joinClassToHql(),
                joinAlias,
                joinCondition
        );
    }

    private String joinClassToHql() {
        return joinClass.getCanonicalName();
    }

    private String joinTypeToHql() {
        return switch (joinType) {
            case DEFAULT -> "JOIN";
            case INNER -> "INNER JOIN";
            case LEFT -> "LEFT JOIN";
            case LEFT_OUTER -> "LEFT OUTER JOIN";
            case RIGHT -> "RIGHT JOIN";
            case RIGHT_OUTER -> "RIGHT OUTER JOIN";
            case FULL -> "FULL JOIN";
            case FULL_OUTER -> "FULL OUTER JOIN";
        };
    }

    @NotNull
    public JoinType getJoinType() {
        return joinType;
    }

    @NotNull
    public Class<?> getJoinClass() {
        return joinClass;
    }

    @NotNull
    public String getJoinAlias() {
        return joinAlias;
    }

    @NotNull
    public String getJoinCondition() {
        return joinCondition;
    }

    @Override
    public String toString() {
        return "JoinClause{" +
                "joinType=" + joinType +
                ", joinClass=" + joinClass +
                ", joinAlias='" + joinAlias + '\'' +
                ", joinCondition='" + joinCondition + '\'' +
                '}';
    }
}
