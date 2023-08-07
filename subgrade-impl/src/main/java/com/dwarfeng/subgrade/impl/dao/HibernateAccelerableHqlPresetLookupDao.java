package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.hql.*;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.DialectNativeLookup;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 使用基于 Hibernate 并且拥有本地加速查询能力的 PresetLookupDao。
 *
 * <p>
 * 该类拥有 {@link HibernateAccelerableHqlPresetLookupDao#nativeLookup} 属性，该属性应当实现所有预设的本地查询功能，
 * 并且在理论上讲，本地查询的效率不应低于 Hibernate 框架的 HQL 查询；同时，该类拥有
 * {@link HibernateAccelerableHqlPresetLookupDao#presetConditionMaker} 属性，该属性作为本地查询不支持时的默认方案，
 * 当本地查询不支持指定的预设时，使用该属性的预设查询方法进行查询。<br>
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * <p>
 * 该类在实际应用时，如果目标数据库是可变的，可能存在针对某一种或几种数据库有本地加速查询，
 * 而另外一种或几种数据库没有本地加速查询的功能。这时候就需要在实例化 {@link PresetLookupDao} 时进行额外的判断，
 * 如果目标数据库支持本地加速查询，则生成 {@link HibernateAccelerableHqlPresetLookupDao}，否则，
 * 生成 {@link HibernatePresetLookupDao}。<br>
 * 实现 {@link DialectNativeLookup}
 * 接口便可方便地判断一个本地查询是否支持指定的数据库（方言）。<br>
 * 利用 {@link HibernateDaoFactory#newHqlPresetLookupDao(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, NativeLookup)}
 * {@link HibernateDaoFactory#newHqlPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, List, String)}
 * {@link HibernateDaoFactory#newHqlPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, List, Class)}
 * 可以便捷地实现目标数据库可变时的本地加速查询判断逻辑。
 *
 * @author DwArFeng
 * @see HibernateDaoFactory#newHqlPresetLookupDao(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, NativeLookup)
 * @see HibernateDaoFactory#newHqlPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, List, String)
 * @see HibernateDaoFactory#newHqlPresetLookupDaoWithChosenDialect(HibernateTemplate, BeanTransformer, Class, PresetConditionMaker, List, Class)
 * @since 1.4.2
 */
public class HibernateAccelerableHqlPresetLookupDao<E extends Entity<?>, PE extends Bean> implements
        PresetLookupDao<E> {

    public static final String DEFAULT_ENTITY_ALIAS = HibernateHqlPresetLookupDao.DEFAULT_ENTITY_ALIAS;

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;
    @Nonnull
    private NativeLookup<E> nativeLookup;
    @Nonnull
    private PresetConditionMaker presetConditionMaker;
    @Nonnull
    private String entityAlias;

    public HibernateAccelerableHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull NativeLookup<E> nativeLookup,
            @Nonnull PresetConditionMaker presetConditionMaker
    ) {
        this(template, entityBeanTransformer, classPE, nativeLookup, presetConditionMaker, DEFAULT_ENTITY_ALIAS);
    }

    public HibernateAccelerableHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull NativeLookup<E> nativeLookup,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull String entityAlias
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.nativeLookup = nativeLookup;
        this.presetConditionMaker = presetConditionMaker;
        this.entityAlias = entityAlias;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } else {
                return template.execute(session -> {
                    // 构建 HqlCondition 对象。
                    HqlCondition condition = new HqlCondition(classPE, entityAlias);

                    // 调用 presetConditionMaker 相应方法，补充查询信息。
                    presetConditionMaker.makeCondition(condition, preset, objs);

                    // 创建 Query 对象。
                    condition.setQueryType(QueryType.ENTITY);
                    HqlQueryInfo queryInfo = HqlQueryInfoFactory.buildQueryInfoFormCondition(condition);
                    // HQL 的安全性由 presetConditionMaker 保证，因此这里不需要进行检查。
                    @SuppressWarnings("SqlSourceToSinkFlow")
                    Query<PE> query = session.createQuery(queryInfo.getHql(), classPE);

                    // 设置参数。
                    for (Map.Entry<String, Object> entry : queryInfo.getParamMap().entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        query.setParameter(paramName, paramValue);
                    }

                    // 执行查询。
                    List<PE> pes = query.getResultList();

                    // 将结果转换为相应的实体。
                    return pes.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
                });
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs, pagingInfo)
                ));
                assert result != null;
                return result;
            } else {
                return template.execute(session -> {
                    // 构建 HqlCondition 对象。
                    HqlCondition condition = new HqlCondition(classPE, entityAlias);

                    // 调用 presetConditionMaker 相应方法，补充查询信息。
                    presetConditionMaker.makeCondition(condition, preset, objs);

                    // 创建 Query 对象。
                    condition.setQueryType(QueryType.ENTITY);
                    HqlQueryInfo queryInfo = HqlQueryInfoFactory.buildQueryInfoFormCondition(condition);
                    // HQL 的安全性由 presetConditionMaker 保证，因此这里不需要进行检查。
                    @SuppressWarnings("SqlSourceToSinkFlow")
                    Query<PE> query = session.createQuery(queryInfo.getHql(), classPE);

                    // 设置参数。
                    for (Map.Entry<String, Object> entry : queryInfo.getParamMap().entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        query.setParameter(paramName, paramValue);
                    }

                    // 设置分页信息。
                    query.setFirstResult(pagingInfo.getPage() * pagingInfo.getRows());
                    query.setMaxResults(pagingInfo.getRows());

                    // 执行查询。
                    List<PE> pes = query.getResultList();

                    // 将结果转换为相应的实体。
                    return pes.stream().map(entityBeanTransformer::reverseTransform).collect(Collectors.toList());
                });
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                Integer result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> nativeLookup.lookupCount(connection, preset, objs)
                ));
                assert result != null;
                return result;
            } else {
                // 获取装箱的结果。
                Integer result = template.execute(session -> {
                    // 构建 HqlCondition 对象。
                    HqlCondition condition = new HqlCondition(classPE, entityAlias);

                    // 调用 presetConditionMaker 相应方法，补充查询信息。
                    presetConditionMaker.makeCondition(condition, preset, objs);

                    // 创建 Query 对象。
                    condition.setQueryType(QueryType.COUNT);
                    HqlQueryInfo queryInfo = HqlQueryInfoFactory.buildQueryInfoFormCondition(condition);
                    // HQL 的安全性由 presetConditionMaker 保证，因此这里不需要进行检查。
                    @SuppressWarnings("SqlSourceToSinkFlow")
                    Query<Long> query = session.createQuery(queryInfo.getHql(), Long.class);

                    // 设置参数。
                    for (Map.Entry<String, Object> entry : queryInfo.getParamMap().entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        query.setParameter(paramName, paramValue);
                    }

                    // 执行查询，返回结果。
                    return query.getSingleResult().intValue();
                });

                // 拆箱。
                return result == null ? 0 : result;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
            if (nativeLookup.supportPreset(preset)) {
                List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                        connection -> this.nativeLookup.lookupEntity(connection, preset, objs, PagingInfo.FIRST_ONE)
                ));
                assert result != null;
                return result.stream().findFirst().orElse(null);
            } else {
                return template.execute(session -> {
                    // 构建 HqlCondition 对象。
                    HqlCondition condition = new HqlCondition(classPE, entityAlias);

                    // 调用 presetConditionMaker 相应方法，补充查询信息。
                    presetConditionMaker.makeCondition(condition, preset, objs);

                    // 创建 Query 对象。
                    condition.setQueryType(QueryType.ENTITY);
                    HqlQueryInfo queryInfo = HqlQueryInfoFactory.buildQueryInfoFormCondition(condition);
                    // HQL 的安全性由 presetConditionMaker 保证，因此这里不需要进行检查。
                    @SuppressWarnings("SqlSourceToSinkFlow")
                    Query<PE> query = session.createQuery(queryInfo.getHql(), classPE);

                    // 设置参数。
                    for (Map.Entry<String, Object> entry : queryInfo.getParamMap().entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        query.setParameter(paramName, paramValue);
                    }

                    // 设置分页信息。
                    query.setFirstResult(0);
                    query.setMaxResults(1);

                    // 执行查询。
                    PE pe = query.getSingleResult();

                    // 将结果转换为相应的实体。
                    return entityBeanTransformer.reverseTransform(pe);
                });
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
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

    @Nonnull
    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@Nonnull Class<PE> classPE) {
        this.classPE = classPE;
    }

    @Nonnull
    public NativeLookup<E> getNativeLookup() {
        return nativeLookup;
    }

    public void setNativeLookup(@Nonnull NativeLookup<E> nativeLookup) {
        this.nativeLookup = nativeLookup;
    }

    @Nonnull
    public PresetConditionMaker getPresetConditionMaker() {
        return presetConditionMaker;
    }

    public void setPresetConditionMaker(@Nonnull PresetConditionMaker presetConditionMaker) {
        this.presetConditionMaker = presetConditionMaker;
    }

    @Nonnull
    public String getEntityAlias() {
        return entityAlias;
    }

    public void setEntityAlias(@Nonnull String entityAlias) {
        this.entityAlias = entityAlias;
    }

    @Override
    public String toString() {
        return "HibernateAccelerableHqlPresetLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", nativeLookup=" + nativeLookup +
                ", presetConditionMaker=" + presetConditionMaker +
                ", entityAlias='" + entityAlias + '\'' +
                '}';
    }
}
