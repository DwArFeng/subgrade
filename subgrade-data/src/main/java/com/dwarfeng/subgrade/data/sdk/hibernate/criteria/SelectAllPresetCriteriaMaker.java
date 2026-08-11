package com.dwarfeng.subgrade.data.sdk.hibernate.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * 实体全选的预设 Criteria 制造器。
 *
 * @param <PE> 持久化实体类型。
 * @author DwArFeng
 * @since 2.0.0
 */
public class SelectAllPresetCriteriaMaker<PE> implements PresetCriteriaMaker<PE> {

    @Override
    public void makeCriteria(
            CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<PE> root, String preset, Object[] objs
    ) {
        // Do nothing.
    }
}
