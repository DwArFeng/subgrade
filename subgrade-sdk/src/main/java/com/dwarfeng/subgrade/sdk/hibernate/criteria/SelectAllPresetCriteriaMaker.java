package com.dwarfeng.subgrade.sdk.hibernate.criteria;

import org.hibernate.criterion.DetachedCriteria;

/**
 * 实体全选的预设 Criteria 制造器。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class SelectAllPresetCriteriaMaker implements PresetCriteriaMaker {

    @Override
    public void makeCriteria(DetachedCriteria criteria, String preset, Object[] objs) {
        //Do nothing.
    }
}
