package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.MatchType;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 大小写敏感 Like 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class CaseSensitiveLikePredicateClause extends AbstractPredicateClause {

    private static final String VARIABLE_NAME = "value";

    @NotNull
    private final String expression;
    @NotNull
    private final String value;
    @NotNull
    private final MatchType matchType;

    public CaseSensitiveLikePredicateClause(
            @NotNull String expression,
            @NotNull String value,
            @NotNull MatchType matchType
    ) {
        this.expression = expression;
        this.value = value;
        this.matchType = matchType;
    }

    @Override
    protected String buildHql() {
        return String.format("%s ILIKE :%s", expression, VARIABLE_NAME);
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put(VARIABLE_NAME, parseValue());
        return Collections.unmodifiableMap(paramMap);
    }

    private String parseValue() {
        return switch (matchType) {
            case EXACT -> value;
            case ANYWHERE -> "%" + value + "%";
            case START -> value + "%";
            case END -> "%" + value;
        };
    }

    @NotNull
    public String getExpression() {
        return expression;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @NotNull
    public MatchType getMatchType() {
        return matchType;
    }

    @Override
    public String toString() {
        return "CaseSensitiveLikePredicateClause{" +
                "expression='" + expression + '\'' +
                ", value='" + value + '\'' +
                ", matchType=" + matchType +
                '}';
    }
}
