package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.NoOrderDetachedCriteria;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 EntireLookupDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class HibernateEntireLookupDao<E extends Entity<?>, PE extends Bean> implements EntireLookupDao<E> {

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;

    public HibernateEntireLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
    }

    @Override
    public List<E> lookup() throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria);
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            @SuppressWarnings("unchecked")
            List<PE> byCriteria = (List<PE>) template.findByCriteria(criteria,
                    pagingInfo.getPage() * pagingInfo.getRows(), pagingInfo.getRows());
            return byCriteria.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount() throws DaoException {
        try {
            DetachedCriteria criteria = NoOrderDetachedCriteria.forClass(classPE);
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
    @Override
    public E lookupFirst() throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
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

    @Override
    public String toString() {
        return "HibernateEntireLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                '}';
    }
}
