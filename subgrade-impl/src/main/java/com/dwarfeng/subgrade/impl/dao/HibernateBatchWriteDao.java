package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 BatchWriteDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class HibernateBatchWriteDao<E extends Entity<?>, PE extends Bean> implements BatchWriteDao<E> {

    /**
     * 默认的批处理数。
     */
    public static final int DEFAULT_BATCH_SIZE = 100;

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnegative
    private int batchSize;

    public HibernateBatchWriteDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer
    ) {
        this(template, entityBeanTransformer, DEFAULT_BATCH_SIZE);
    }

    public HibernateBatchWriteDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnegative int batchSize
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.batchSize = batchSize;
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

    @Override
    public void batchWrite(List<E> elements) throws DaoException {
        try {
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                if (i % batchSize == 0) {
                    template.flush();
                    template.clear();
                }
                PE pe = collect.get(i);
                template.save(pe);
            }
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private PE transformEntity(E entity) {
        return entityBeanTransformer.transform(entity);
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

    @Nonnegative
    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(@Nonnegative int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public String toString() {
        return "HibernateBatchWriteDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", batchSize=" + batchSize +
                '}';
    }
}
