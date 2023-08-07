package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.JoinType;

import javax.annotation.Nonnull;

/**
 * Join 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class JoinClause extends AbstractClause {

    @Nonnull
    private final JoinType joinType;
    @Nonnull
    private final Class<?> joinClass;
    @Nonnull
    private final String joinAlias;
    @Nonnull
    private final String joinCondition;

    public JoinClause(
            @Nonnull JoinType joinType,
            @Nonnull Class<?> joinClass,
            @Nonnull String joinAlias,
            @Nonnull String joinCondition
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
        switch (joinType) {
            case DEFAULT:
                return "JOIN";
            case INNER:
                return "INNER JOIN";
            case LEFT:
                return "LEFT JOIN";
            case LEFT_OUTER:
                return "LEFT OUTER JOIN";
            case RIGHT:
                return "RIGHT JOIN";
            case RIGHT_OUTER:
                return "RIGHT OUTER JOIN";
            case FULL:
                return "FULL JOIN";
            default:
                throw new IllegalArgumentException("非法的枚举值: " + joinType);
        }
    }

    @Nonnull
    public JoinType getJoinType() {
        return joinType;
    }

    @Nonnull
    public Class<?> getJoinClass() {
        return joinClass;
    }

    @Nonnull
    public String getJoinAlias() {
        return joinAlias;
    }

    @Nonnull
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
