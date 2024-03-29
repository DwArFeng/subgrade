package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 通过 Hibernate 实现的单对象数据访问层。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class HibernateSingleObjectDao<K extends Key, E extends Entity<K>, PK extends Bean, PE extends Bean> implements
        SingleObjectDao<E> {

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<K, PK> keyBeanTransformer;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private K key;
    @Nonnull
    private Class<PE> classPE;

    public HibernateSingleObjectDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<K, PK> keyBeanTransformer,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull K key,
            @Nonnull Class<PE> classPE
    ) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.key = key;
        this.classPE = classPE;
    }

    @Override
    public boolean exists() throws DaoException {
        try {
            return internalExists();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalExists() {
        return Objects.nonNull(template.get(classPE, transformKey(key)));
    }

    @Override
    public E get() throws DaoException {
        try {
            return internalGet();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void put(E entity) throws DaoException {
        try {
            @SuppressWarnings("unchecked")
            E newEntity = (E) BeanUtils.cloneBean(entity);
            newEntity.setKey(key);
            template.clear();
            template.saveOrUpdate(newEntity);
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void clear() throws DaoException {
        try {
            if (!internalExists()) {
                throw new DaoException("实体对象不存在");
            }

            PK pk = transformKey(key);
            //noinspection ConstantConditions PE 不可能为null，因为之前的语句已经判断PE一定存在。
            template.delete(template.get(classPE, pk));
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private E internalGet() {
        PK pk = transformKey(key);
        return reverseTransformEntity(template.get(classPE, pk));
    }

    private PK transformKey(K k) {
        return keyBeanTransformer.transform(k);
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
    public K getKey() {
        return key;
    }

    public void setKey(@Nonnull K key) {
        this.key = key;
    }

    @Nonnull
    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@Nonnull Class<PE> classPE) {
        this.classPE = classPE;
    }

    @Override
    public String toString() {
        return "HibernateSingleObjectDao{" +
                "template=" + template +
                ", keyBeanTransformer=" + keyBeanTransformer +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", key=" + key +
                ", classPE=" + classPE +
                '}';
    }
}
