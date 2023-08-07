package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.MatchType;

import javax.annotation.Nonnull;
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

    @Nonnull
    private final String expression;
    @Nonnull
    private final String value;
    @Nonnull
    private final MatchType matchType;

    public CaseSensitiveLikePredicateClause(
            @Nonnull String expression,
            @Nonnull String value,
            @Nonnull MatchType matchType
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

    @SuppressWarnings("DuplicatedCode")
    private String parseValue() {
        switch (matchType) {
            case EXACT:
                return value;
            case ANYWHERE:
                return "%" + value + "%";
            case START:
                return value + "%";
            case END:
                return "%" + value;
            default:
                throw new IllegalArgumentException("非法的匹配类型: " + matchType);
        }
    }

    @Nonnull
    public String getExpression() {
        return expression;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
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
