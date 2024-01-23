package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.sdk.hibernate.modification.DeletionMod;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
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

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<K, PK> keyBeanTransformer;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;
    @Nonnull
    private DeletionMod<PE> deletionMod;
    @Nonnull
    private Collection<String> updateKeepFields;

    public HibernateBaseDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<K, PK> keyBeanTransformer,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE
    ) {
        this(
                template, keyBeanTransformer, entityBeanTransformer, classPE, new DefaultDeletionMod<>(),
                Collections.emptySet()
        );
    }

    public HibernateBaseDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<K, PK> keyBeanTransformer,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull DeletionMod<PE> deletionMod,
            @Nonnull Collection<String> updateKeepFields
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
                    Object oldValue = PropertyUtils.getProperty(oldPe, updateKeepField);
                    PropertyUtils.setProperty(pe, updateKeepField, oldValue);
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

    @Nonnull
    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull HibernateTemplate template) {
        this.template = template;
    }

    @Nonnull
    public BeanTransformer<K, PK> getKeyBeanTransformer() {
        return keyBeanTransformer;
    }

    public void setKeyBeanTransformer(@Nonnull BeanTransformer<K, PK> keyBeanTransformer) {
        this.keyBeanTransformer = keyBeanTransformer;
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

    @Nonnull
    public DeletionMod<PE> getDeletionMod() {
        return deletionMod;
    }

    public void setDeletionMod(@Nonnull DeletionMod<PE> deletionMod) {
        this.deletionMod = deletionMod;
    }

    @Nonnull
    public Collection<String> getUpdateKeepFields() {
        return updateKeepFields;
    }

    public void setUpdateKeepFields(@Nonnull Collection<String> updateKeepFields) {
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
