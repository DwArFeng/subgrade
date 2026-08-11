package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.internal.bean.BeanPropertyAccess;
import com.dwarfeng.subgrade.data.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.data.sdk.hibernate.modification.DeletionMod;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 BatchBaseDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
@SuppressWarnings("DuplicatedCode")
public class HibernateBatchBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements
        BatchBaseDao<K, E> {

    /**
     * 默认的批处理数。
     *
     * @since 0.3.2-beta
     */
    public static final int DEFAULT_BATCH_SIZE = 100;

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
    @Range(from = 0, to = Long.MAX_VALUE)
    private int batchSize;
    @NotNull
    private Collection<String> updateKeepFields;

    public HibernateBatchBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE
    ) {
        this(template, keyBeanTransformer, entityBeanTransformer, classPE, new DefaultDeletionMod<>(),
                DEFAULT_BATCH_SIZE, Collections.emptySet());
    }

    public HibernateBatchBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull DeletionMod<PE> deletionMod
    ) {
        this(template, keyBeanTransformer, entityBeanTransformer,
                classPE, deletionMod, DEFAULT_BATCH_SIZE, Collections.emptySet());
    }

    public HibernateBatchBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull DeletionMod<PE> deletionMod,
            @NotNull Collection<String> updateKeepFields

    ) {
        this(
                template, keyBeanTransformer, entityBeanTransformer, classPE, deletionMod, DEFAULT_BATCH_SIZE,
                updateKeepFields
        );
    }

    public HibernateBatchBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull DeletionMod<PE> deletionMod,
            @Range(from = 0, to = Long.MAX_VALUE) int batchSize
    ) {
        this(
                template, keyBeanTransformer, entityBeanTransformer, classPE, deletionMod, batchSize,
                Collections.emptySet()
        );
    }

    public HibernateBatchBaseDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<K, PK> keyBeanTransformer,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull DeletionMod<PE> deletionMod,
            @Range(from = 0, to = Long.MAX_VALUE) int batchSize,
            @NotNull Collection<String> updateKeepFields
    ) {
        this.template = template;
        this.keyBeanTransformer = keyBeanTransformer;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.deletionMod = deletionMod;
        this.batchSize = batchSize;
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
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).toList();
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
            List<PE> collect = elements.stream().map(entityBeanTransformer::transform).toList();
            List<PE> pesToUpdate = new ArrayList<>();
            for (int i = 0; i < collect.size(); i++) {
                if (i % batchSize == 0) {
                    template.clear();
                    pesToUpdate.forEach(template::update);
                    template.flush();
                    template.clear();
                    pesToUpdate.clear();
                }
                PE pe = collect.get(i);
                if (!updateKeepFields.isEmpty()) {
                    PE oldPe = internalGet(elements.get(i).getKey());
                    for (String updateKeepField : updateKeepFields) {
                        Object oldValue = BeanPropertyAccess.getInstance().getProperty(oldPe, updateKeepField);
                        BeanPropertyAccess.getInstance().setProperty(pe, updateKeepField, oldValue);
                    }
                }
                pesToUpdate.add(pe);
            }
            template.clear();
            pesToUpdate.forEach(template::update);
            template.flush();
            template.clear();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            List<Object> objectsToUpdate = new ArrayList<>();
            List<PE> pesToDelete = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                if (i % batchSize == 0) {
                    template.clear();
                    objectsToUpdate.forEach(template::update);
                    pesToDelete.forEach(template::delete);
                    template.flush();
                    template.clear();
                    objectsToUpdate.clear();
                    pesToDelete.clear();
                }
                K key = keys.get(i);
                PE pe = internalGet(key);
                objectsToUpdate.addAll(deletionMod.updateBeforeDelete(pe));
                pesToDelete.add(pe);
            }
            template.clear();
            objectsToUpdate.forEach(template::update);
            pesToDelete.forEach(template::delete);
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

    @Range(from = 0, to = Long.MAX_VALUE)
    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(@Range(from = 0, to = Long.MAX_VALUE) int batchSize) {
        this.batchSize = batchSize;
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
        return "HibernateBatchBaseDao{" +
                "template=" + template +
                ", keyBeanTransformer=" + keyBeanTransformer +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", deletionMod=" + deletionMod +
                ", batchSize=" + batchSize +
                ", updateKeepFields=" + updateKeepFields +
                '}';
    }
}
