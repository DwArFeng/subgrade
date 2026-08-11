package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.data.sdk.hibernate.hql.PresetConditionMaker;
import com.dwarfeng.subgrade.data.sdk.hibernate.nativelookup.DialectNativeLookup;
import com.dwarfeng.subgrade.data.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.PresetLookupDao;
import org.hibernate.dialect.Dialect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName,
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass,
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
     * @param template              HibernateOperations 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetCriteriaMaker   预设 Criteria 制造器。
     * @param nativeLookup          本地查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newPresetLookupDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker,
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName,
            @NotNull String entityAlias
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass,
            @NotNull String entityAlias
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName,
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
     * @param template                  HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull String dialectClassCanonicalName,
            boolean accelerateEnabled,
            @NotNull String entityAlias
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass,
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @NotNull List<DialectNativeLookup<E>> nativeSqlLookups,
            @NotNull Class<? extends Dialect> dialectClass,
            boolean accelerateEnabled,
            @NotNull String entityAlias
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
     * @param template              HibernateOperations 操作类。
     * @param entityBeanTransformer 实体与持久化 Bean 的 Bean 映射器。
     * @param classPE               持久化 Bean 的类。
     * @param presetConditionMaker  预设 Condition 制造器。
     * @param nativeLookup          本地查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean> PresetLookupDao<E> newHqlPresetLookupDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
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
     * @param template              HibernateOperations 操作类。
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
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull PresetConditionMaker presetConditionMaker,
            @Nullable NativeLookup<E> nativeLookup,
            @NotNull String entityAlias
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
