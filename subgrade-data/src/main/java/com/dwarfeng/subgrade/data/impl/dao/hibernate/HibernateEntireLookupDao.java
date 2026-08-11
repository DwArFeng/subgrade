package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * 使用 Jakarta Criteria 实现的 Hibernate 全量查询数据访问层。
 *
 * <p>
 * 该类只提供查询实现，不负责创建事务。调用方应通过代理或其它事务管理机制绑定 Hibernate 当前会话。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class HibernateEntireLookupDao<E extends Entity<?>, PE extends Bean> implements EntireLookupDao<E> {

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @NotNull
    private Class<PE> classPE;

    public HibernateEntireLookupDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
    }

    @Override
    public List<E> lookup() throws DaoException {
        try {
            return template.execute(session -> createEntityQuery(session).getResultList().stream()
                    .map(entityBeanTransformer::reverseTransform).toList());
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        int page = pagingInfo.getPage();
        int rows = pagingInfo.getRows();
        if (rows <= 0) {
            return Collections.emptyList();
        }
        try {
            return template.execute(session -> createEntityQuery(session)
                    .setFirstResult(page * rows)
                    .setMaxResults(rows)
                    .getResultList()
                    .stream()
                    .map(entityBeanTransformer::reverseTransform)
                    .toList());
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public int lookupCount() throws DaoException {
        try {
            return template.execute(session -> {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
                Root<PE> root = criteriaQuery.from(classPE);
                criteriaQuery.select(criteriaBuilder.count(root));
                return session.createSelectionQuery(criteriaQuery).getSingleResult().intValue();
            });
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public E lookupFirst() throws DaoException {
        try {
            return template.execute(session -> createEntityQuery(session)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .map(entityBeanTransformer::reverseTransform)
                    .orElse(null));
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    private SelectionQuery<PE> createEntityQuery(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<PE> criteriaQuery = criteriaBuilder.createQuery(classPE);
        Root<PE> root = criteriaQuery.from(classPE);
        criteriaQuery.select(root);
        return session.createSelectionQuery(criteriaQuery);
    }

    @NotNull
    public HibernateOperations getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull HibernateOperations template) {
        this.template = template;
    }

    @NotNull
    public BeanTransformer<E, PE> getEntityBeanTransformer() {
        return entityBeanTransformer;
    }

    public void setEntityBeanTransformer(@NotNull BeanTransformer<E, PE> entityBeanTransformer) {
        this.entityBeanTransformer = entityBeanTransformer;
    }

    @NotNull
    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@NotNull Class<PE> classPE) {
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
