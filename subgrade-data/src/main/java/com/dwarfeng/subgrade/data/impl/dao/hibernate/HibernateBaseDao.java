package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.internal.bean.BeanPropertyAccess;
import com.dwarfeng.subgrade.data.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.data.sdk.hibernate.modification.DeletionMod;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.BaseDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 使用 Hibernate 实现的 BaseDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class HibernateBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements
        BaseDao<K, E> {

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<K, PK> keyBeanTransformer;
    @NotNull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @NotNull
    private Class<PE> classPE;
    @NotNull
    private DeletionMod<PE> deletionMod;
    @NotNull
    private Collection<String> updateKeepFields;

    public HibernateBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE
    ) {
        this(
                template, keyBeanTransformer, entityBeanTransformer, classPE, new DefaultDeletionMod<>(),
                Collections.emptySet()
        );
    }

    public HibernateBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull DeletionMod<PE> deletionMod,
            @NotNull Collection<String> updateKeepFields
    ) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.deletionMod = deletionMod;
        this.updateKeepFields = updateKeepFields;
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
            if (!updateKeepFields.isEmpty()) {
                PE oldPe = internalGet(element.getKey());
                for (String updateKeepField : updateKeepFields) {
                    Object oldValue = BeanPropertyAccess.getInstance().getProperty(oldPe, updateKeepField);
                    BeanPropertyAccess.getInstance().setProperty(pe, updateKeepField, oldValue);
                }
            }
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
            template.clear();
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

    @NotNull
    public HibernateOperations getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull HibernateOperations template) {
        this.template = template;
    }

    @NotNull
    public BeanTransformer<K, PK> getKeyBeanTransformer() {
        return keyBeanTransformer;
    }

    public void setKeyBeanTransformer(@NotNull BeanTransformer<K, PK> keyBeanTransformer) {
        this.keyBeanTransformer = keyBeanTransformer;
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

    @NotNull
    public DeletionMod<PE> getDeletionMod() {
        return deletionMod;
    }

    public void setDeletionMod(@NotNull DeletionMod<PE> deletionMod) {
        this.deletionMod = deletionMod;
    }

    @NotNull
    public Collection<String> getUpdateKeepFields() {
        return updateKeepFields;
    }

    public void setUpdateKeepFields(@NotNull Collection<String> updateKeepFields) {
        this.updateKeepFields = updateKeepFields;
    }

    @Override
    public String toString() {
        return "HibernateBaseDao{" +
                "template=" + template +
                ", keyBeanTransformer=" + keyBeanTransformer +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", deletionMod=" + deletionMod +
                ", updateKeepFields=" + updateKeepFields +
                '}';
    }
}
