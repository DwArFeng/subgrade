package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.NoOrderDetachedCriteria;
import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.DialectNativeLookup;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用基于 Hibernate 并且拥有本地加速查询能力的 PresetLookupDao。
 *
 * <p>
 * 该类拥有 {@link HibernateAccelerablePresetLookupDao#nativeLookup} 属性，该属性应当实现所有预设的本地查询功能，
 * 并且在理论上讲，本地查询的效率不应低于 Hibernate 框架的 Criteria 查询；同时，该类拥有
 * {@link HibernateAccelerablePresetLookupDao#presetCriteriaMaker} 属性，该属性作为本地查询不支持时的默认方案，
 * 当本地查询不支持指定的预设时，使用该属性的预设查询方法进行查询。<br>
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * <p>
 * 该类在实际应用时，如果目标数据库是可变的，可能存在针对某一种或几种数据库有本地加速查询，
 * 而另外一种或几种数据库没有本地加速查询的功能。这时候就需要在实例化 {@link PresetLookupDao} 时进行额外的判断，
 * 如果目标数据库支持本地加速查询，则生成 {@link HibernateAccelerablePresetLookupDao}，否则，
 * 生成 {@link HibernatePresetLookupDao}。<br>
 * 实现 {@link DialectNativeLookup}
 * 接口便可方便地判断一个本地查询是否支持指定的数据库（方言）。<br>
 * 利用 {@link HibernateDaoFactory#newPresetLookupDao(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, NativeLookup)}
 * {@link HibernateDaoFactory#newPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, List, String)}
 * {@link HibernateDaoFactory#newPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, List, Class)}
 * 可以便捷地实现目标数据库可变时的本地加速查询判断逻辑。
 *
 * @author DwArFeng
 * @see HibernateDaoFactory#newPresetLookupDao(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, NativeLookup)
 * @see HibernateDaoFactory#newPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, List, String)
 * @see HibernateDaoFactory#newPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetCriteriaMaker, List, Class)
 * @since 1.4.2
 */
public class HibernateAccelerablePresetLookupDao<E extends Entity<?>, PE extends Bean> implements
        PresetLookupDao<E> {

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;
    @Nonnull
    private NativeLookup<E> nativeLookup;
    @Nonnull
    private PresetCriteriaMaker presetCriteriaMaker;

    public HibernateAccelerablePresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull NativeLookup<E> nativeLookup,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.nativeLookup = nativeLookup;
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } else {
                DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
                presetCriteriaMaker.makeCriteria(criteria, preset, objs);
                @SuppressWarnings("unchecked")
                List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
                return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs, pagingInfo)
                ));
                assert result != null;
                return result;
            } else {
                DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
                presetCriteriaMaker.makeCriteria(criteria, preset, objs);
                @SuppressWarnings("unchecked")
                List<PE> byCriteria = (List<PE>) template.findByCriteria(
                        criteria, pagingInfo.getPage() * pagingInfo.getRows(), pagingInfo.getRows()
                );
                return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                Integer result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> nativeLookup.lookupCount(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } else {
                DetachedCriteria criteria = NoOrderDetachedCriteria.forClass(classPE);
                presetCriteriaMaker.makeCriteria(criteria, preset, objs);
                criteria.setProjection(Projections.rowCount());
                return template.findByCriteria(criteria).stream().findFirst().map(Long.class::cast)
                        .map(Long::intValue).orElse(0);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs, PagingInfo.FIRST_ONE)
                ));
                assert result != null;
                return result.stream().findFirst().orElse(null);
            } else {
                DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
                presetCriteriaMaker.makeCriteria(criteria, preset, objs);
                @SuppressWarnings("unchecked")
                List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria, 0, 1);
                return byCriteria.stream().findFirst().map(entityBeanTransformer::reverseTransform).orElse(null);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Nonnull
    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull HibernateTemplate template) {
        this.template = template;
    }

    @Nonnull
    public BeanTransformer<E, PE> getEntityBeanTransformer() {
        return entityBeanTransformer;
    }

    public void setEntityBeanTransformer(@Nonnull BeanTransformer<E, PE> entityBeanTransformer) {
        this.entityBeanTransformer = entityBeanTransformer;
    }

    @Nonnull
    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@Nonnull Class<PE> classPE) {
        this.classPE = classPE;
    }

    @Nonnull
    public NativeLookup<E> getNativeLookup() {
        return nativeLookup;
    }

    public void setNativeLookup(@Nonnull NativeLookup<E> nativeLookup) {
        this.nativeLookup = nativeLookup;
    }

    @Nonnull
    public PresetCriteriaMaker getPresetCriteriaMaker() {
        return presetCriteriaMaker;
    }

    public void setPresetCriteriaMaker(@Nonnull PresetCriteriaMaker presetCriteriaMaker) {
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @Override
    public String toString() {
        return "HibernateAccelerablePresetLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", nativeLookup=" + nativeLookup +
                ", presetCriteriaMaker=" + presetCriteriaMaker +
                '}';
    }
}
