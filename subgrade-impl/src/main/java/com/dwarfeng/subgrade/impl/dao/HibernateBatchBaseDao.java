package com.dwarfeng.subgrade.impl.dao;

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
 * 使用 Hibernate 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateBatchBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements BatchBaseDao<K, E> {

    private HibernateTemplate template;
    private BeanTransformer<K, PK> keyBeanTransformer;
    private BeanTransformer<E, PE> entityBeanTransformer;
    private Class<PE> classPE;

    public HibernateBatchBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<K, PK> keyBeanTransformer,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            @SuppressWarnings("unchecked")
            PK pk = (PK) template.save(pe);
            template.flush();
            return reverseTransformKey(pk);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public K update(E element) throws DaoException {
        try {
            PE pe = transformEntity(element);
            template.clear();
            template.update(pe);
            template.flush();
            return element.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            PE pe = internalGet(key);
            template.delete(pe);
            template.flush();
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
            for (PE pe : collect) {
                //noinspection unchecked
                listPK.add((PK) template.save(pe));
            }
            template.flush();
            return listPK.stream().map(keyBeanTransformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<K> batchUpdate(List<E> elements) throws DaoException {
        try {
            template.clear();
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).collect(Collectors.toList());
            for (PE pe : collect) {
                template.update(pe);
            }
            template.flush();
            return elements.stream().map(E::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            for (K key : keys) {
                PE pe = internalGet(key);
                template.delete(pe);
            }
            template.flush();
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
    public List<E> batchGet(List<K> keys) {
        List<PE> list = new ArrayList<>();
        for (K key : keys) {
            list.add(internalGet(key));
        }
        return list.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
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
}