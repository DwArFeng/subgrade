package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.sdk.hibernate.modification.DeletionMod;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 BatchBaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class HibernateBatchBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements BatchBaseDao<K, E> {

    /**
     * 默认的批处理数。
     *
     * @since 0.3.2-beta
     */
    public static final int DEFAULT_BATCH_SIZE = 100;

    private HibernateTemplate template;
    private BeanTransformer<K, PK> keyBeanTransformer;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;
    private DeletionMod<PE> deletionMod;
    private int batchSize;

    public HibernateBatchBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE) {
        this(template, keyBeanTransformer, entityBeanTransformer, classPE, new DefaultDeletionMod<>(),
                DEFAULT_BATCH_SIZE);
    }

    public HibernateBatchBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull DeletionMod<PE> deletionMod) {
        this(template, keyBeanTransformer, entityBeanTransformer, classPE, deletionMod, DEFAULT_BATCH_SIZE);
    }

    public HibernateBatchBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull DeletionMod<PE> deletionMod,
            int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("参数 batchSize 必须为正数");
        }
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.deletionMod = deletionMod;
        this.batchSize = batchSize;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            @SuppressWarnings("unchecked")
            PK pk = (PK) template.save(pe);
            template.flush();
            template.clear();
            K key = reverseTransformKey(pk);
            element.setKey(key);
            return key;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            template.clear();
            template.update(pe);
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            PE pe = internalGet(key);
            List<Object> objects = deletionMod.updateBeforeDelete(pe);
            objects.forEach(template::update);
            template.delete(pe);
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exists(K key) throws DaoException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            PE pe = internalGet(key);
            return reverseTransformEntity(pe);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private PE internalGet(K key) {
        PK pk = transformKey(key);
        return template.get(classPE, pk);
    }

    private boolean internalExists(K key) {
        return Objects.nonNull(template.get(classPE, transformKey(key)));
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws DaoException {
        try {
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).collect(Collectors.toList());
            List<PK> listPK = new ArrayList<>();
            for (int i = 0; i < collect.size(); i++) {
                if (i % batchSize == 0) {
                    template.flush();
                    template.clear();
                }
                PE pe = collect.get(i);
                @SuppressWarnings("unchecked")
                PK save = (PK) template.save(pe);
                listPK.add(save);
            }
            template.flush();
            template.clear();
            List<K> ks = listPK.stream().map(keyBeanTransformer::reverseTransform).collect(Collectors.toList());
            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).setKey(ks.get(i));
            }
            return ks;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchUpdate(List<E> elements) throws DaoException {
        try {
            template.clear();
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                if (i % batchSize == 0) {
                    template.flush();
                    template.clear();
                }
                PE pe = collect.get(i);
                List<Object> objects = deletionMod.updateBeforeDelete(pe);
                objects.forEach(template::update);
                template.update(pe);
            }
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            for (int i = 0; i < keys.size(); i++) {
                if (i % batchSize == 0) {
                    template.flush();
                    template.clear();
                }
                K key = keys.get(i);
                PE pe = internalGet(key);
                template.delete(pe);
            }
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean allExists(List<K> keys) throws DaoException {
        try {
            for (K key : keys) {
                if (!internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean nonExists(List<K> keys) throws DaoException {
        try {
            for (K key : keys) {
                if (internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> batchGet(List<K> keys) throws DaoException {
        try {
            List<PE> list = new ArrayList<>();
            for (K key : keys) {
                list.add(internalGet(key));
            }
            return list.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private PK transformKey(K k) {
        return keyBeanTransformer.transform(k);
    }

    private K reverseTransformKey(PK pk) {
        return keyBeanTransformer.reverseTransform(pk);
    }

    private PE transformEntity(E entity) {
        return entityBeanTransformer.transform(entity);
    }

    private E reverseTransformEntity(PE persistenceEntity) {
        return entityBeanTransformer.reverseTransform(persistenceEntity);
    }

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull HibernateTemplate template) {
        this.template = template;
    }

    public BeanTransformer<K, PK> getKeyBeanTransformer() {
        return keyBeanTransformer;
    }

    public void setKeyBeanTransformer(@NonNull BeanTransformer<K, PK> keyBeanTransformer) {
        this.keyBeanTransformer = keyBeanTransformer;
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

    public DeletionMod<PE> getDeletionMod() {
        return deletionMod;
    }

    public void setDeletionMod(@NonNull DeletionMod<PE> deletionMod) {
        this.deletionMod = deletionMod;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
