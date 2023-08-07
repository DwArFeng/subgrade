package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * HQL 条件。
 *
 * <p>
 * 该类不是线程安全的，请在一个线程内使用该类。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public final class HqlCondition {

    private final ReferenceModel<Class<?>> entityClassRef = new DefaultReferenceModel<>();
    private final ReferenceModel<String> entityAliasRef = new DefaultReferenceModel<>();
    private final ReferenceModel<QueryType> queryTypeReferenceModel = new DefaultReferenceModel<>();
    private final List<Clause> joinClauses = new ArrayList<>();
    private final List<PredicateClause> whereClauses = new ArrayList<>();
    private final List<Clause> groupByClauses = new ArrayList<>();
    private final List<PredicateClause> havingClauses = new ArrayList<>();
    private final List<Clause> orderByClauses = new ArrayList<>();

    public HqlCondition() {
    }

    public HqlCondition(Class<?> entityClass) {
        this.entityClassRef.set(entityClass);
    }

    public HqlCondition(Class<?> entityClass, String entityAlias) {
        this.entityClassRef.set(entityClass);
        this.entityAliasRef.set(entityAlias);
    }

    public HqlCondition setEntityClass(Class<?> entityClass) {
        this.entityClassRef.set(entityClass);
        return this;
    }

    public HqlCondition setEntityAlias(String entityAlias) {
        this.entityAliasRef.set(entityAlias);
        return this;
    }

    public HqlCondition setQueryType(QueryType queryType) {
        this.queryTypeReferenceModel.set(queryType);
        return this;
    }

    public HqlCondition addJoinClause(Clause joinClause) {
        this.joinClauses.add(joinClause);
        return this;
    }

    public HqlCondition addWhereClause(PredicateClause whereClause) {
        this.whereClauses.add(whereClause);
        return this;
    }

    public HqlCondition addGroupByClause(Clause groupByClause) {
        this.groupByClauses.add(groupByClause);
        return this;
    }

    public HqlCondition addHavingClause(PredicateClause havingClause) {
        this.havingClauses.add(havingClause);
        return this;
    }

    public HqlCondition addOrderByClause(Clause orderByClause) {
        this.orderByClauses.add(orderByClause);
        return this;
    }

    public ReferenceModel<Class<?>> getEntityClassRef() {
        return entityClassRef;
    }

    public ReferenceModel<String> getEntityAliasRef() {
        return entityAliasRef;
    }

    public ReferenceModel<QueryType> getQueryTypeReferenceModel() {
        return queryTypeReferenceModel;
    }

    public List<Clause> getJoinClauses() {
        return joinClauses;
    }

    public List<PredicateClause> getWhereClauses() {
        return whereClauses;
    }

    public List<Clause> getGroupByClauses() {
        return groupByClauses;
    }

    public List<PredicateClause> getHavingClauses() {
        return havingClauses;
    }

    public List<Clause> getOrderByClauses() {
        return orderByClauses;
    }

    @Override
    public String toString() {
        return "HqlCondition{" +
                "entityClassRef=" + entityClassRef +
                ", entityAliasRef=" + entityAliasRef +
                ", queryTypeReferenceModel=" + queryTypeReferenceModel +
                ", joinClauses=" + joinClauses +
                ", whereClauses=" + whereClauses +
                ", groupByClauses=" + groupByClauses +
                ", havingClauses=" + havingClauses +
                ", orderByClauses=" + orderByClauses +
                '}';
    }
}
