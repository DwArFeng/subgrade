package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.PredicateClause;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * 逻辑与谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class AndPredicateClause extends AbstractPredicateClause {

    private static final String PARAM_INDICATOR = ":";
    private static final String PARAM_NAME_DELIMITER = "_";

    // 此处必须使用列表，以保证迭代的顺序。
    @Nonnull
    private final List<PredicateClause> predicateClauses;

    public AndPredicateClause(@Nonnull Collection<PredicateClause> predicateClauses) {
        this.predicateClauses = new ArrayList<>(predicateClauses);
    }

    @Override
    protected String buildHql() {
        StringBuilder hqlBuilder = new StringBuilder();

        // 拼接左括号。
        hqlBuilder.append("(");

        // 如果谓词子句为空，则拼接 1=1；否则，拼接谓词子句。
        if (predicateClauses.isEmpty()) {
            hqlBuilder.append("1=1");
        } else {
            for (int i = 0; i < predicateClauses.size(); i++) {
                // 获取谓词子句。
                PredicateClause predicateClause = predicateClauses.get(i);

                // 展开参数。
                String hql = predicateClause.toHql();
                Map<String, Object> paramMap = predicateClause.toParamMap();

                // 定义并获取新的 HQL。
                String neoHql = getNeoHql(hql, paramMap, i);

                // 拼接新 HQL。
                hqlBuilder.append(neoHql);

                // 如果不是最后一个元素，则拼接分隔符。
                if (i < predicateClauses.size() - 1) {
                    hqlBuilder.append(" AND ");
                }
            }
        }

        // 拼接右括号。
        hqlBuilder.append(")");

        // 返回结果。
        return hqlBuilder.toString();
    }

    @SuppressWarnings("DuplicatedCode")
    private String getNeoHql(String hql, Map<String, Object> paramMap, int i) {
        String neoHql = hql;

        // 遍历参数映射，替换新 HQL 中的参数名。
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            // 展开参数。
            String param = entry.getKey();

            // 定义新的参数名，以索引为前缀，拼接 PARAM_NAME_DELIMITER 作为分隔符，再拼接原参数名。
            String neoParam = i + PARAM_NAME_DELIMITER + param;

            // 替换 neoHql 中的参数名。
            String paramWithIndicator = PARAM_INDICATOR + param;
            String neoParamWithIndicator = PARAM_INDICATOR + neoParam;
            neoHql = neoHql.replace(paramWithIndicator, neoParamWithIndicator);
        }
        return neoHql;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected Map<String, Object> buildParamMap() {
        // 定义参数映射。
        Map<String, Object> paramMap = new LinkedHashMap<>();

        // 遍历谓词子句，将参数映射添加到 paramMap 中。
        for (int i = 0; i < predicateClauses.size(); i++) {
            PredicateClause predicateClause = predicateClauses.get(i);
            // 展开参数。
            Map<String, Object> subParamMap = predicateClause.toParamMap();

            // 遍历参数映射，将参数映射添加到 paramMap 中。
            for (Map.Entry<String, Object> entry : subParamMap.entrySet()) {
                // 展开参数。
                String param = entry.getKey();
                Object value = entry.getValue();

                // 定义新的参数名，以索引为前缀，拼接 PARAM_NAME_DELIMITER 作为分隔符，再拼接原参数名。
                String neoParam = i + PARAM_NAME_DELIMITER + param;

                // 将参数映射添加到 paramMap 中。
                paramMap.put(neoParam, value);
            }
        }

        // 返回结果。
        return Collections.unmodifiableMap(paramMap);
    }

    @Nonnull
    public List<PredicateClause> getPredicateClauses() {
        return predicateClauses;
    }

    @Override
    public String toString() {
        return "AndPredicateClause{" +
                "predicateClauses=" + predicateClauses +
                '}';
    }
}
