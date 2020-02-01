package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.modification.DefaultDeletion;
import com.dwarfeng.subgrade.sdk.hibernate.modification.Deletion;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;
import java.util.Objects;

/**
 * 使用 Hibernate 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements BaseDao<K, E> {

    private HibernateTemplate template;
    private BeanTransformer<K, PK> keyBeanTransformer;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;
    private Deletion<PE> deletion;

    public HibernateBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        deletion = new DefaultDeletion<>();
    }

    public HibernateBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull Deletion<PE> deletion) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.deletion = deletion;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            @SuppressWarnings("unchecked")
            PK pk = (PK) template.save(pe);
            template.flush();
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
            template.update(pe);
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            PE pe = internalGet(key);
            List<Object> objects = deletion.updateBeforeDelete(pe);
            objects.forEach(template::update);
            template.delete(pe);
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exists(K key) throws DaoException {
        try {
            return Objects.nonNull(template.get(classPE, transformKey(key)));
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

    public Deletion<PE> getDeletion() {
        return deletion;
    }

    public void setDeletion(Deletion<PE> deletion) {
        this.deletion = deletion;
    }
}
