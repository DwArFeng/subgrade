package com.dwarfeng.subgrade.sdk.hibernate.hql.clause;

import com.dwarfeng.subgrade.sdk.hibernate.hql.PredicateClause;

import javax.annotation.Nonnull;
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
 * @since 1.8.5
 */
public class ExistsPredicateClause extends AbstractPredicateClause {

    @Nonnull
    private final Class<?> entityClass;
    @Nonnull
    private final String entityAlias;
    @Nonnull
    private final PredicateClause predicateClause;

    public ExistsPredicateClause(
            @Nonnull Class<?> entityClass, @Nonnull String entityAlias, @Nonnull PredicateClause predicateClause
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

    @Nonnull
    public Class<?> getEntityClass() {
        return entityClass;
    }

    @Nonnull
    public String getEntityAlias() {
        return entityAlias;
    }

    @Nonnull
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
