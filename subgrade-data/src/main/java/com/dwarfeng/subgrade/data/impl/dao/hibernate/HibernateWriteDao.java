package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.WriteDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;

import org.jetbrains.annotations.NotNull;

/**
 * 使用 Hibernate 实现的 WriteDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class HibernateWriteDao<E extends Entity<?>, PE extends Bean> implements WriteDao<E> {

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<E, PE> entityBeanTransformer;

    public HibernateWriteDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
    }

    @Override
    public void write(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            template.save(pe);
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private PE transformEntity(E entity) {
        return entityBeanTransformer.transform(entity);
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

    @Override
    public String toString() {
        return "HibernateWriteDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                '}';
    }
}
