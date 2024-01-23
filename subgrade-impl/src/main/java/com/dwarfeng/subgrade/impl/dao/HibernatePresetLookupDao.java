package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.NoOrderDetachedCriteria;
import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
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
 * 使用 Hibernate 实现的 PresetLookupDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * <p>
 * 该类使用 Hibernate Criteria API 实现。基于 Hibernate Criteria，该类可以实现大多数的查询需求。
 * 有关 Criteria API 的使用，请参考
 * <a href="https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#appendix-legacy-
 * criteria">Hibernate 官方文档</a>。
 *
 * <p>
 * 如果需要实现 Criteria API 无法实现的查询需求，请使用 {@link HibernateHqlPresetLookupDao}。
 *
 * <p>
 * 自 Hibernate 5.0.0.Final 开始，Hibernate 官方已经不推荐使用 Hibernate Criteria API，而是推荐使用 JPA Criteria API。
 * 但 Hibernate 仍然保留了 Hibernate Criteria API，且没有将其标记为 deprecated，因此该类仍然可以使用。<br>
 * 随着版本升级，当 Hibernate Criteria API 被官方标记为 deprecated 时，该类将被标记为 deprecated。
 *
 * <p>
 * 使用者应在项目的升级中，逐步将此类替换为 {@link HibernateHqlPresetLookupDao}。
 *
 * @author DwArFeng
 * @see HibernateHqlPresetLookupDao
 * @since 0.0.3-beta
 */
public class HibernatePresetLookupDao<E extends Entity<?>, PE extends Bean> implements PresetLookupDao<E> {

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;
    @Nonnull
    private PresetCriteriaMaker presetCriteriaMaker;

    public HibernatePresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
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
            DetachedCriteria criteria = NoOrderDetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(
                    criteria, pagingInfo.getPage() * pagingInfo.getRows(), pagingInfo.getRows()
            );
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            criteria.setProjection(Projections.rowCount());
            return template.findByCriteria(criteria).stream().findFirst().map(Long.class::cast)
                    .map(Long::intValue).orElse(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    /**
     * @since 1.2.8
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria, 0, 1);
            return byCriteria.stream().findFirst().map(entityBeanTransformer::reverseTransform).orElse(null);
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
    public PresetCriteriaMaker getPresetCriteriaMaker() {
        return presetCriteriaMaker;
    }

    public void setPresetCriteriaMaker(@Nonnull PresetCriteriaMaker presetCriteriaMaker) {
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @Override
    public String toString() {
        return "HibernatePresetLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", presetCriteriaMaker=" + presetCriteriaMaker +
                '}';
    }
}
