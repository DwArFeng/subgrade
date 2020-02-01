package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.sdk.hibernate.modification.DeletionMod;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.PresetDeleteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 PresetDeleteDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class HibernatePresetDeleteDao<K extends Key, E extends Entity<K>, PE extends Bean> implements PresetDeleteDao<K, E> {

    private HibernateTemplate template;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;
    private PresetCriteriaMaker presetCriteriaMaker;
    private DeletionMod<PE> deletionMod;

    public HibernatePresetDeleteDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull PresetCriteriaMaker presetCriteriaMaker) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.presetCriteriaMaker = presetCriteriaMaker;
        deletionMod = new DefaultDeletionMod<>();
    }

    public HibernatePresetDeleteDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull PresetCriteriaMaker presetCriteriaMaker,
            @NonNull DeletionMod<PE> deletionMod) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.presetCriteriaMaker = presetCriteriaMaker;
        this.deletionMod = deletionMod;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            //noinspection unchecked
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            //noinspection unchecked
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
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            criteria.setProjection(Projections.rowCount());
            return template.findByCriteria(criteria).stream().findFirst().map(Long.class::cast)
                    .map(Long::intValue).orElse(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<K> lookupDelete(String preset, Object[] objs) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            presetCriteriaMaker.makeCriteria(criteria, preset, objs);
            //noinspection unchecked
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
            Optional<List<Object>> reduce = byCriteria.stream().map(deletionMod::updateBeforeDelete).reduce((a, b) -> {
                a.addAll(b);
                return a;
            });
            reduce.ifPresent(objects -> objects.forEach(template::update));
            template.deleteAll(byCriteria);
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).map(E::getKey).collect(Collectors.toList());
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

    public DeletionMod<PE> getDeletionMod() {
        return deletionMod;
    }

    public void setDeletionMod(@NonNull DeletionMod<PE> deletionMod) {
        this.deletionMod = deletionMod;
    }
}
