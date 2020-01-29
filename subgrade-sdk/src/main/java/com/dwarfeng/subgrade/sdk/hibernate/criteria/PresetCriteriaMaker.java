package com.dwarfeng.subgrade.sdk.hibernate.criteria;

import org.hibernate.criterion.DetachedCriteria;

/**
 * Preset制造器。
 * <p>根据预设名称与预设对象构造Criteria。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface PresetCriteriaMaker {

    /**
     * 制造Criteria。
     *
     * @param criteria 指定的Criteria。
     * @param preset   预设的名称。
     * @param objs     预设对应的值。
     */
    void makeCriteria(DetachedCriteria criteria, String preset, Object[] objs);
}
