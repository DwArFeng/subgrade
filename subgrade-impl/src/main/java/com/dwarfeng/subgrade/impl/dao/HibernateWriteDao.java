package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.WriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

/**
 * 使用 Hibernate 实现的 WriteDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class HibernateWriteDao<E extends Entity<?>, PE extends Bean> implements WriteDao<E> {

    private HibernateTemplate template;
    private BeanTransformer<E, PE> entityBeanTransformer;

    public HibernateWriteDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer) {
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

    private PE transformEntity(E entity) {
        return entityBeanTransformer.transform(entity);
    }
}
