package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import org.springframework.orm.hibernate5.HibernateTemplate;

/**
 * 使用基于 Hibernate 并且拥有本地加速查询能力的 PresetLookupDao。
 *
 * <p>
 * 该类由于命名不规范，已经被 {@link HibernateAccelerablePresetLookupDao} 取代。
 *
 * @deprecated 该类由于命名不规范，已经被 {@link HibernateAccelerablePresetLookupDao} 取代。
 * @see HibernateAccelerablePresetLookupDao
 * @author DwArFeng
 * @since 1.2.8
 */
public final class HibernateAcceleratePresetLookupDao<E extends Entity<?>, PE extends Bean> extends
        HibernateAccelerablePresetLookupDao<E, PE> {

    public HibernateAcceleratePresetLookupDao(
            HibernateTemplate template,
            BeanTransformer<E, PE> entityBeanTransformer,
            Class<PE> classPE,
            NativeLookup<E> nativeLookup,
            PresetCriteriaMaker presetCriteriaMaker
    ) {
        super(template, entityBeanTransformer, classPE, nativeLookup, presetCriteriaMaker);
    }
}
