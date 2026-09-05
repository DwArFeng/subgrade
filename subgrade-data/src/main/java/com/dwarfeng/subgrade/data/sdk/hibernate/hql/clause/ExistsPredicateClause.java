package com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.PredicateClause;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 存在谓词子句。
 *
 * <p>
 * 该谓词通过实体类型、实体别名和内部谓词构造相关子查询。内部谓词中的参数会随该谓词继续向外传递，
 * 并由 HQL 查询信息工厂统一重新分配名称。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ExistsPredicateClause extends AbstractPredicateClause {

    @NotNull
    private final Class<?> entityClass;
    @NotNull
    private final String entityAlias;
    @NotNull
    private final PredicateClause predicateClause;

    public ExistsPredicateClause(
            @NotNull Class<?> entityClass, @NotNull String entityAlias, @NotNull PredicateClause predicateClause
    ) {
        this.entityClass = entityClass;
        this.entityAlias = entityAlias;
        this.predicateClause = predicateClause;
    }

    @Override
    protected String buildHql() {
        return "EXISTS (SELECT " + entityAlias +
                " FROM " + entityClass.getName() + " AS " + entityAlias +
                " WHERE " + predicateClause.toHql() + ")";
    }

    @Override
    protected Map<String, Object> buildParamMap() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(predicateClause.toParamMap()));
    }

    @NotNull
    public Class<?> getEntityClass() {
        return entityClass;
    }

    @NotNull
    public String getEntityAlias() {
        return entityAlias;
    }

    @NotNull
    public PredicateClause getPredicateClause() {
        return predicateClause;
    }

    @Override
    public String toString() {
        return "ExistsPredicateClause{" +
                "entityClass=" + entityClass +
                ", entityAlias='" + entityAlias + '\'' +
                ", predicateClause=" + predicateClause +
                '}';
    }
}
