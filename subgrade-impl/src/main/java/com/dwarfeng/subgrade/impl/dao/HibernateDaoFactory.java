package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.sdk.hibernate.hql.PresetConditionMaker;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.DialectNativeLookup;
import com.dwarfeng.subgrade.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import org.hibernate.dialect.Dialect;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Hibernate 数据访问层工厂。
 *
 * @author DwArFeng
 * @since 1.2.8
 */
public final class HibernateDaoFactory {

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 PresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetCriteriaMaker       预设 Criteria 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClassCanonicalName)
        ).findFirst().orElse(null);
        return newPresetLookupDao(template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeLookup);
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 PresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetCriteriaMaker   预设 Criteria 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClass)
        ).findFirst().orElse(null);
        return newPresetLookupDao(template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeLookup);
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 PresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetCriteriaMaker       预设 Criteria 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param accelerateEnabled         是否启用加速模式。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName,
            boolean accelerateEnabled
    ) {
        if (accelerateEnabled) {
            return newPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeSqlLookups,
                    dialectClassCanonicalName
            );
        } else {
            return new HibernatePresetLookupDao<>(template, entityBeanTransformer, classPE, presetCriteriaMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 PresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetCriteriaMaker   预设 Criteria 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param accelerateEnabled     是否启用加速模式。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass,
            boolean accelerateEnabled
    ) {
        if (accelerateEnabled) {
            return newPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeSqlLookups,
                    dialectClass
            );
        } else {
            return new HibernatePresetLookupDao<>(template, entityBeanTransformer, classPE, presetCriteriaMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 PresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetCriteriaMaker   预设 Criteria 制造器。
     * @param nativeLookup          本地查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetCriteriaMaker presetCriteriaMaker,
            @Nullable NativeLookup<E> nativeLookup
    ) {
        if (Objects.isNull(nativeLookup)) {
            return new HibernatePresetLookupDao<>(template, entityBeanTransformer, classPE, presetCriteriaMaker);
        } else {
            return new HibernateAccelerablePresetLookupDao<>(
                    template, entityBeanTransformer, classPE, nativeLookup, presetCriteriaMaker
            );
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetConditionMaker      预设 Condition 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClassCanonicalName)
        ).findFirst().orElse(null);
        return newHqlPresetLookupDao(template, entityBeanTransformer, classPE, presetConditionMaker, nativeLookup);
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetConditionMaker      预设 Condition 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @param entityAlias               实体的别名。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName,
            @Nonnull String entityAlias
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClassCanonicalName)
        ).findFirst().orElse(null);
        return newHqlPresetLookupDao(
                template, entityBeanTransformer, classPE, presetConditionMaker, nativeLookup, entityAlias
        );
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClass)
        ).findFirst().orElse(null);
        return newHqlPresetLookupDao(template, entityBeanTransformer, classPE, presetConditionMaker, nativeLookup);
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @param entityAlias           实体的别名。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass,
            @Nonnull String entityAlias
    ) {
        NativeLookup<E> nativeLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClass)
        ).findFirst().orElse(null);
        return newHqlPresetLookupDao(
                template, entityBeanTransformer, classPE, presetConditionMaker, nativeLookup, entityAlias
        );
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetConditionMaker      预设 Condition 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param accelerateEnabled         是否启用加速模式。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName,
            boolean accelerateEnabled
    ) {
        if (accelerateEnabled) {
            return newHqlPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetConditionMaker, nativeSqlLookups,
                    dialectClassCanonicalName
            );
        } else {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template                  HibernateTemplate 操作类。
     * @param entityBeanTransformer     实体与持久化 Bean 的 Bean 映射器。
     * @param classPE                   持久化 Bean 的类。
     * @param presetConditionMaker      预设 Condition 制造器。
     * @param nativeSqlLookups          由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param accelerateEnabled         是否启用加速模式。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @param entityAlias               实体的别名。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull String dialectClassCanonicalName,
            boolean accelerateEnabled,
            @Nonnull String entityAlias
    ) {
        if (accelerateEnabled) {
            return newHqlPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetConditionMaker, nativeSqlLookups,
                    dialectClassCanonicalName, entityAlias
            );
        } else {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param accelerateEnabled     是否启用加速模式。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass,
            boolean accelerateEnabled
    ) {
        if (accelerateEnabled) {
            return newHqlPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetConditionMaker, nativeSqlLookups,
                    dialectClass
            );
        } else {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeSqlLookups      由可选的 DialectNativeLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param accelerateEnabled     是否启用加速模式。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @param entityAlias           实体的别名。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeLookup
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDaoWithChosenDialect(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nonnull List<DialectNativeLookup<E>> nativeSqlLookups,
            @Nonnull Class<? extends Dialect> dialectClass,
            boolean accelerateEnabled,
            @Nonnull String entityAlias
    ) {
        if (accelerateEnabled) {
            return newHqlPresetLookupDaoWithChosenDialect(
                    template, entityBeanTransformer, classPE, presetConditionMaker, nativeSqlLookups,
                    dialectClass, entityAlias
            );
        } else {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeLookup          本地查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nullable NativeLookup<E> nativeLookup
    ) {
        if (Objects.isNull(nativeLookup)) {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        } else {
            return new HibernateAccelerableHqlPresetLookupDao<>(
                    template, entityBeanTransformer, classPE, nativeLookup, presetConditionMaker
            );
        }
    }

    /**
     * 根据传入的参数生成一个新的预设查询数据访问层。
     *
     * <p>
     * 根据输入参数的不同，生成不同的基于 Hibernate 的 HqlPresetLookupDao 的实现。
     *
     * @param template              HibernateTemplate 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeLookup          本地查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @param entityAlias           实体的别名。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<E, PE> entityBeanTransformer,
            @Nonnull Class<PE> classPE,
            @Nonnull PresetConditionMaker presetConditionMaker,
            @Nullable NativeLookup<E> nativeLookup,
            @Nonnull String entityAlias
    ) {
        if (Objects.isNull(nativeLookup)) {
            return new HibernateHqlPresetLookupDao<>(template, entityBeanTransformer, classPE, presetConditionMaker);
        } else {
            return new HibernateAccelerableHqlPresetLookupDao<>(
                    template, entityBeanTransformer, classPE, nativeLookup, presetConditionMaker, entityAlias
            );
        }
    }

    private HibernateDaoFactory() {
        throw new IllegalStateException("禁止实例化");
    }
}
