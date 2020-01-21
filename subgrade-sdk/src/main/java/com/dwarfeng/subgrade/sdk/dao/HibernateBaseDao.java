package com.dwarfeng.subgrade.sdk.dao;

import com.dwarfeng.subgrade.sdk.interceptor.TimeAnalyse;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanMapper;
import com.dwarfeng.subgrade.stack.bean.dto.LookupPagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class HibernateBaseDao<K extends Key, PK extends Bean, E extends Entity<K>, PE extends Bean> implements BaseDao<K, E> {

    private HibernateTemplate template;
    private BeanMapper<K, PK> keyMapper;
    private BeanMapper<E, PE> entityMapper;
    private Class<PE> classPE;

    public HibernateBaseDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanMapper<K, PK> keyMapper,
            @NonNull BeanMapper<E, PE> entityMapper,
            @NonNull Class<K> clazzK,
            @NonNull Class<E> classE,
            @NonNull Class<PK> classPK,
            @NonNull Class<PE> classPE) {
        this.template = template;
        this.keyMapper = keyMapper;
        this.entityMapper = entityMapper;
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
            PE pe = rawGet(key);
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
            PE pe = rawGet(key);
            return reverseTransformEntity(pe);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> getAll() throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            @SuppressWarnings("unchecked")
            List<PE> peList = (List<PE>) template.findByCriteria(criteria);
            return peList.stream().map(this::reverseTransformEntity).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    @TimeAnalyse
    public int countAll() throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            criteria.setProjection(Projections.rowCount());
            return template.findByCriteria(criteria)
                    .stream().findFirst().map(Long.class::cast).map(Long::intValue).orElse(0);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> get(LookupPagingInfo lookupPagingInfo) throws DaoException {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(classPE);
            @SuppressWarnings("unchecked")
            List<PE> peList = (List<PE>) template.findByCriteria(criteria,
                    lookupPagingInfo.getPage() * lookupPagingInfo.getRows(),
                    lookupPagingInfo.getRows());
            return peList.stream().map(this::reverseTransformEntity).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private PE rawGet(K key) {
        PK pk = transformKey(key);
        return template.get(classPE, pk);
    }

    private PK transformKey(K k) {
        return keyMapper.transform(k);
    }

    private K reverseTransformKey(PK pk) {
        return keyMapper.reverseTransform(pk);
    }

    private PE transformEntity(E entity) {
        return entityMapper.transform(entity);
    }

    private E reverseTransformEntity(PE persistenceEntity) {
        return entityMapper.reverseTransform(persistenceEntity);
    }

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull HibernateTemplate template) {
        this.template = template;
    }

    public BeanMapper<K, PK> getKeyMapper() {
        return keyMapper;
    }

    public void setKeyMapper(@NonNull BeanMapper<K, PK> keyMapper) {
        this.keyMapper = keyMapper;
    }

    public BeanMapper<E, PE> getEntityMapper() {
        return entityMapper;
    }

    public void setEntityMapper(@NonNull BeanMapper<E, PE> entityMapper) {
        this.entityMapper = entityMapper;
    }

    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@NonNull Class<PE> classPE) {
        this.classPE = classPE;
    }
}
