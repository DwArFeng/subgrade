package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.OptionalCriteriaMaker;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.OptionalLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.criterion.*;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 OptionalLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class HibernateOptionalLookupDao<E extends Entity<?>, PE extends Bean> implements OptionalLookupDao<E> {

    private HibernateTemplate template;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;
    private OptionalCriteriaMaker optionalCriteriaMaker;

    public HibernateOptionalLookupDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull OptionalCriteriaMaker optionalCriteriaMaker) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.optionalCriteriaMaker = optionalCriteriaMaker;
    }

    @Override
    public List<E> lookup(Map<String, Object[]> optional) throws DaoException {
        return lookup(optional, Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap());
    }

    @Override
    public List<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            processCriterion(andOptional, orOptional, notOptional, criteria);
            processOrder(orderOptional, criteria);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(Map<String, Object[]> optional, PagingInfo pagingInfo) throws DaoException {
        return lookup(optional, Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap(), pagingInfo);
    }

    @Override
    public List<E> lookup(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            Map<String, Object[]> orderOptional,
            PagingInfo pagingInfo) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            processCriterion(andOptional, orOptional, notOptional, criteria);
            processOrder(orderOptional, criteria);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria,
                    pagingInfo.getPage() * pagingInfo.getRows(), pagingInfo.getRows());
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(Map<String, Object[]> optional) throws DaoException {
        return lookupCount(optional, Collections.emptyList(), Collections.emptyMap());
    }

    @Override
    public int lookupCount(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            processCriterion(andOptional, orOptional, notOptional, criteria);
            criteria.setProjection(Projections.rowCount());
            return template.findByCriteria(criteria).stream().findFirst().map(Long.class::cast)
                    .map(Long::intValue).orElse(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void processCriterion(
            Map<String, Object[]> andOptional,
            List<Map<String, Object[]>> orOptional,
            Map<String, Object[]> notOptional,
            DetachedCriteria criteria) {
        //处理 notOptional。
        for (Map.Entry<String, Object[]> entry : notOptional.entrySet()) {
            criteria.add(Restrictions.not(makeCriterionFromEntry(entry)));
        }
        //处理 orOptional。
        for (Map<String, Object[]> map : orOptional) {
            List<Criterion> criterionList = new ArrayList<>();
            for (Map.Entry<String, Object[]> entry : map.entrySet()) {
                criterionList.add(makeCriterionFromEntry(entry));
            }
            criteria.add(Restrictions.or(criterionList.toArray(new Criterion[0])));
        }
        //处理 andOptional。
        for (Map.Entry<String, Object[]> entry : andOptional.entrySet()) {
            criteria.add(makeCriterionFromEntry(entry));
        }
    }

    private void processOrder(Map<String, Object[]> orderOptional, DetachedCriteria criteria) {
        //处理 orderOptional。
        for (Map.Entry<String, Object[]> entry : orderOptional.entrySet()) {
            criteria.addOrder(makeOrderFromEntry(entry));
        }
    }

    private Criterion makeCriterionFromEntry(Map.Entry<String, Object[]> entry) {
        return optionalCriteriaMaker.makeCriterion(entry.getKey(), entry.getValue());
    }

    private Order makeOrderFromEntry(Map.Entry<String, Object[]> entry) {
        return optionalCriteriaMaker.makeOrder(entry.getKey(), entry.getValue());
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

    public OptionalCriteriaMaker getOptionalCriteriaMaker() {
        return optionalCriteriaMaker;
    }

    public void setOptionalCriteriaMaker(@NonNull OptionalCriteriaMaker optionalCriteriaMaker) {
        this.optionalCriteriaMaker = optionalCriteriaMaker;
    }
}
