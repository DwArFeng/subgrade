package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.hql.*;
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

public class HibernateHqlPresetLookupDao<E extends Entity<?>, PE extends Bean> implements PresetLookupDao<E> {

    public static final String DEFAULT_ENTITY_ALIAS = "pojo";

    @Nonnull
    private HibernateTemplate template;
    @Nonnull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @Nonnull
    private Class<PE> classPE;
    @Nonnull
    private PresetConditionMaker presetConditionMaker;
    @Nonnull
    private String entityAlias;

    public HibernateHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker
    ) {
        this(template, entityBeanTransformer, classPE, presetConditionMaker, DEFAULT_ENTITY_ALIAS);
    }

    public HibernateHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull String entityAlias
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.presetConditionMaker = presetConditionMaker;
        this.entityAlias = entityAlias;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
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
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
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
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            // 获取装箱的结果。
            Integer result = template.execute(session -> {
                // 构建 HqlCondition 对象。
                HqlCondition condition = new NoOrderHqlCondition(classPE, entityAlias);

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
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
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
        return "HibernateHqlPresetLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", presetConditionMaker=" + presetConditionMaker +
                ", entityAlias='" + entityAlias + '\'' +
                '}';
    }
}
