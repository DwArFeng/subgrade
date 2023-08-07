package com.dwarfeng.subgrade.sdk.hibernate.hql;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HQL 查询信息工厂。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public class HqlQueryInfoFactory {

    private static final String PARAM_INDICATOR = ":";
    private static final String PARAM_PREFIX = "var_";

    @SuppressWarnings("DuplicatedCode")
    public static HqlQueryInfo buildQueryInfoFormCondition(HqlCondition condition) {
        // 展开参数。
        Class<?> entityClass = condition.getEntityClassRef().get();
        String entityAlias = condition.getEntityAliasRef().get();
        QueryType queryType = condition.getQueryTypeReferenceModel().get();
        List<Clause> joinClauses = condition.getJoinClauses();
        List<PredicateClause> whereClauses = condition.getWhereClauses();
        List<Clause> groupByClauses = condition.getGroupByClauses();
        List<PredicateClause> havingClauses = condition.getHavingClauses();
        List<Clause> orderByClauses = condition.getOrderByClauses();

        // 参数校验。
        Objects.requireNonNull(entityClass, "实体类不能为 null。");
        Objects.requireNonNull(entityAlias, "实体别名不能为 null。");
        Objects.requireNonNull(queryType, "查询类型不能为 null。");

        // 定义中间变量。
        AtomicInteger paramIndex = new AtomicInteger(0);
        StringBuilder hqlBuilder = new StringBuilder();
        Map<String, Object> paramMap = new LinkedHashMap<>();

        // Entity query: SELECT {entityAlias} FROM {entityClass} AS {entityAlias}.
        // COUNT query: SELECT COUNT(*) FROM {entityClass} AS {entityAlias}.
        hqlBuilder.append("SELECT ");
        switch (queryType) {
            case ENTITY:
                hqlBuilder.append(entityAlias);
                break;
            case COUNT:
                hqlBuilder.append("COUNT(*)");
                break;
            default:
                throw new AssertionError("非法的查询类型: " + queryType);
        }
        hqlBuilder
                .append(" FROM ")
                .append(entityClass.getName())
                .append(" AS ")
                .append(entityAlias);

        // May JOIN.
        if (!joinClauses.isEmpty()) {
            hqlBuilder.append(" ");
            for (int i = 0; i < joinClauses.size(); i++) {
                Clause joinClause = joinClauses.get(i);
                hqlBuilder.append(joinClause.toHql());
                // 除了最后一个元素以外，剩下的元素增加分隔符。
                if (i < joinClauses.size() - 1) {
                    hqlBuilder.append(" ");
                }
            }
        }

        // May "WHERE".
        if (!whereClauses.isEmpty()) {
            hqlBuilder.append(" WHERE ");
            for (int i = 0; i < whereClauses.size(); i++) {
                PredicateClause whereClause = whereClauses.get(i);

                // 展开参数。
                String innerHql = whereClause.toHql();
                Map<String, Object> innerParamMap = whereClause.toParamMap();

                // 重新分配参数。
                ParamReassignResult paramReassignResult = reassignParam(innerHql, innerParamMap, paramIndex);

                // 将重新分配的参数后的 HQL 和 参数映射添加到最终的 HQL 和 参数映射中。
                hqlBuilder.append(paramReassignResult.getHql());
                paramMap.putAll(paramReassignResult.getParamMap());

                // 除了最后一个元素以外，剩下的元素增加分隔符。
                if (i < whereClauses.size() - 1) {
                    hqlBuilder.append(" AND ");
                }
            }
        }

        // May "GROUP BY".
        if (!groupByClauses.isEmpty()) {
            hqlBuilder.append(" GROUP BY ");
            for (int i = 0; i < groupByClauses.size(); i++) {
                Clause groupByClause = groupByClauses.get(i);
                hqlBuilder.append(groupByClause.toHql());
                // 除了最后一个元素以外，剩下的元素增加分隔符。
                if (i < groupByClauses.size() - 1) {
                    hqlBuilder.append(", ");
                }
            }
        }

        // May "HAVING".
        if (!havingClauses.isEmpty()) {
            hqlBuilder.append(" HAVING ");
            for (int i = 0; i < havingClauses.size(); i++) {
                PredicateClause havingClause = havingClauses.get(i);

                // 展开参数。
                String innerHql = havingClause.toHql();
                Map<String, Object> innerParamMap = havingClause.toParamMap();

                // 重新分配参数。
                ParamReassignResult paramReassignResult = reassignParam(innerHql, innerParamMap, paramIndex);

                // 将重新分配的参数后的 HQL 和 参数映射添加到最终的 HQL 和 参数映射中。
                hqlBuilder.append(paramReassignResult.getHql());
                paramMap.putAll(paramReassignResult.getParamMap());

                // 除了最后一个元素以外，剩下的元素增加分隔符。
                if (i < havingClauses.size() - 1) {
                    hqlBuilder.append(" AND ");
                }
            }
        }

        // May "ORDER BY".
        if (!orderByClauses.isEmpty() && queryType == QueryType.ENTITY) {
            hqlBuilder.append(" ORDER BY ");
            for (int i = 0; i < orderByClauses.size(); i++) {
                Clause orderByClause = orderByClauses.get(i);
                hqlBuilder.append(orderByClause.toHql());
                // 除了最后一个元素以外，剩下的元素增加分隔符。
                if (i < orderByClauses.size() - 1) {
                    hqlBuilder.append(", ");
                }
            }
        }

        // 返回最终的 HQL 和 参数映射。
        return new HqlQueryInfo(hqlBuilder.toString(), Collections.unmodifiableMap(paramMap));
    }

    private static ParamReassignResult reassignParam(
            String hql, Map<String, Object> paramMap, AtomicInteger paramIndex
    ) {
        // 创建临时变量。
        String neoHql = hql;
        Map<String, Object> neoParamMap = new LinkedHashMap<>();

        // 遍历参数映射。
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            // 展开参数。
            String param = entry.getKey();
            Object value = entry.getValue();

            // 新参数名，以 PARAM_PREFIX 为前缀，拼接 paramIndex，paramIndex 并自增。
            String neoParam = PARAM_PREFIX + (paramIndex.getAndIncrement());

            // 替换 neoHql 中的参数名。
            String paramWithIndicator = PARAM_INDICATOR + param;
            String neoParamWithIndicator = PARAM_INDICATOR + neoParam;
            neoHql = neoHql.replace(paramWithIndicator, neoParamWithIndicator);

            // 将参数映射添加到 neoParamMap 中。
            neoParamMap.put(neoParam, value);
        }

        // 返回结果。
        return new ParamReassignResult(neoHql, neoParamMap);
    }

    private static final class ParamReassignResult {

        @Nonnull
        private final String hql;
        @Nonnull
        private final Map<String, Object> paramMap;

        public ParamReassignResult(@Nonnull String hql, @Nonnull Map<String, Object> paramMap) {
            this.hql = hql;
            this.paramMap = paramMap;
        }

        @Nonnull
        public String getHql() {
            return hql;
        }

        @Nonnull
        public Map<String, Object> getParamMap() {
            return paramMap;
        }

        @Override
        public String toString() {
            return "ParamReassignResult{" +
                    "hql='" + hql + '\'' +
                    ", paramMap=" + paramMap +
                    '}';
        }
    }

    private HqlQueryInfoFactory() {
        throw new IllegalStateException("禁止实例化");
    }
}
