package com.dwarfeng.subgrade.data.sdk.hibernate.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * 预设 Criteria 制造器。
 *
 * <p>
 * 根据预设名称与参数配置 Jakarta Criteria 查询。实现可以设置查询条件、排序、分组和其它 Criteria 属性。
 *
 * @param <PE> 持久化实体类型。
 * @author DwArFeng
 * @since 2.0.0
 */
@FunctionalInterface
public interface PresetCriteriaMaker<PE> {

    /**
     * 配置 Criteria 查询。
     *
     * @param criteriaBuilder Criteria 构建器。
     * @param criteriaQuery   Criteria 查询。
     * @param root            持久化实体根节点。
     * @param preset          预设名称。
     * @param objs            预设参数。
     */
    void makeCriteria(
            CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<PE> root, String preset, Object[] objs
    );
}
