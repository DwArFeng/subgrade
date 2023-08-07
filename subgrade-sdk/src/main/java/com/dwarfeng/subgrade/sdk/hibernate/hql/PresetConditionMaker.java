package com.dwarfeng.subgrade.sdk.hibernate.hql;

/**
 * 预设 Condition 制造器。
 *
 * <p>
 * 根据预设名称与预设对象构造 Condition。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public interface PresetConditionMaker {

    /**
     * 制造 Condition。
     *
     * @param condition 指定的 Condition。
     * @param preset    预设的名称。
     * @param objs      预设对应的值。
     */
    void makeCondition(HqlCondition condition, String preset, Object[] objs);
}
