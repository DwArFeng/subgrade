package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

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

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Range(from = 0, to = Long.MAX_VALUE)
    private int batchSize;

    public HibernateBatchWriteDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer
    ) {
        this(template, entityBeanTransformer, DEFAULT_BATCH_SIZE);
    }

    public HibernateBatchWriteDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @Range(from = 0, to = Long.MAX_VALUE) int batchSize
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
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).toList();
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

    @Range(from = 0, to = Long.MAX_VALUE)
    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(@Range(from = 0, to = Long.MAX_VALUE) int batchSize) {
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
