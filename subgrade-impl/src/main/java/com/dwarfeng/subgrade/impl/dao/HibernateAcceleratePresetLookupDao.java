package com.dwarfeng.subgrade.impl.dao;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用基于 Hibernate 并且拥有本地加速查询能力的 PresetLookupDao。
 *
 * <p>
 * 该类拥有 {@link HibernateAcceleratePresetLookupDao#nativeLookup} 属性，该属性应当实现所有预设的本地查询功能，
 * 并且在理论上讲，本地查询的效率不应低于 Hibernate 框架的 Criteria 查询；同时，该类拥有
 * {@link HibernateAcceleratePresetLookupDao#presetCriteriaMaker} 属性，该属性作为兜底，当本地查询发生错误时，
 * 使用该属性的预设查询方法进行兜底性质的查询，虽然效率比本地查询慢，但不至于出错。<br>
 * 当一个预设查询被调用时，nativeLookup 的查询方法被首先调用，数据访问层尝试使用本地查询直接返回结果，
 * 并捕获期间发生的任何异常（根据 {@link HibernateTemplate} 的文档，如代码执行过程中需要抛出任何的自定义的异常，
 * 需要保证所有的异常都是 {@link RuntimeException}）。当一个异常被捕获时，该异常被妥善的记录在日志中，
 * 数据访问层随后会调用 presetCriteriaMaker，使用 Hibernate 框架的 Criteria 查询数据，并返回对应的结果。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * <p>
 * 该类在实际应用时，如果目标数据库是可变的，可能存在针对某一种或几种数据库有本地加速查询，
 * 而另外一种或几种数据库没有本地加速查询的功能。这时候就需要在实例化 {@link PresetLookupDao} 时进行额外的判断，
 * 如果目标数据库支持本地加速查询，则生成 {@link HibernateAcceleratePresetLookupDao}，否则，
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
 * @since 1.2.8
 */
public class HibernateAcceleratePresetLookupDao<E extends Entity<?>, PE extends Bean> implements
        PresetLookupDao<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateAcceleratePresetLookupDao.class);

    private HibernateTemplate template;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;
    private NativeLookup<E> nativeLookup;
    private PresetCriteriaMaker presetCriteriaMaker;

    public HibernateAcceleratePresetLookupDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull NativeLookup<E> nativeLookup,
            @NonNull PresetCriteriaMaker presetCriteriaMaker
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
            try {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } catch (Exception e) {
                LOGGER.warn(
                        "使用本地查询时发生异常，本次将使用 Criteria 进行查询。预设: " + preset +
                                ", 参数: " + Arrays.toString(objs) + ", 异常信息如下:", e
                );
            }
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            try {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs, pagingInfo)
                ));
                assert result != null;
                return result;
            } catch (Exception e) {
                LOGGER.warn(
                        "使用本地查询时发生异常，本次将使用 Criteria 进行查询。预设: " + preset +
                                ", 参数: " + Arrays.toString(objs) + ", 异常信息如下:", e
                );
            }
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria,
                    pagingInfo.getPage() * pagingInfo.getRows(), pagingInfo.getRows());
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            try {
                Integer result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> nativeLookup.lookupCount(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } catch (Exception e) {
                LOGGER.warn(
                        "使用本地查询时发生异常，本次将使用 Criteria 进行查询。预设: " + preset +
                                ", 参数: " + Arrays.toString(objs) + ", 异常信息如下:", e
                );
            }
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            criteria.setProjection(Projections.rowCount());
            return template.findByCriteria(criteria).stream().findFirst().map(Long.class::cast)
                    .map(Long::intValue).orElse(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull HibernateTemplate template) {
        this.template = template;
    }

    public BeanTransformer<E, PE> getEntityBeanTransformer() {
        return entityBeanTransformer;
    }

    public void setEntityBeanTransformer(@NonNull BeanTransformer<E, PE> entityBeanTransformer) {
        this.entityBeanTransformer = entityBeanTransformer;
    }

    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@NonNull Class<PE> classPE) {
        this.classPE = classPE;
    }

    public PresetCriteriaMaker getPresetCriteriaMaker() {
        return presetCriteriaMaker;
    }

    public void setPresetCriteriaMaker(@NonNull PresetCriteriaMaker presetCriteriaMaker) {
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    public NativeLookup<E> getNativeSqlLookup() {
        return nativeLookup;
    }

    public void setNativeSqlLookup(@NonNull NativeLookup<E> nativeLookup) {
        this.nativeLookup = nativeLookup;
    }
}
