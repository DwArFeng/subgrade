package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.sdk.hibernate.nativesql.DialectNativeSqlLookup;
import com.dwarfeng.subgrade.sdk.hibernate.nativesql.NativeSqlLookup;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import org.hibernate.dialect.Dialect;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.orm.hibernate5.HibernateTemplate;

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
     * @param nativeSqlLookups          由可选的 DialectNativeSqlLookup 组成的集合。
     * @param dialectClassCanonicalName 当前使用的方言的类的正式名称。
     * @param <E>                       实体的泛型。
     * @param <PE>                      持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeSqlLookup
     */
    public static <E extends Entity<?>, PE extends Bean & Entity<?>> PresetLookupDao<E>
    newPresetLookupDaoWithChosenDialect(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull PresetCriteriaMaker presetCriteriaMaker,
            @NonNull List<DialectNativeSqlLookup<PE>> nativeSqlLookups,
            @NonNull String dialectClassCanonicalName
    ) {
        NativeSqlLookup<PE> nativeSqlLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClassCanonicalName)
        ).findFirst().orElse(null);
        return newPresetLookupDao(template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeSqlLookup);
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
     * @param nativeSqlLookups      由可选的 DialectNativeSqlLookup 组成的集合。
     * @param dialectClass          当前使用的方言的类。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     * @see DialectNativeSqlLookup
     */
    public static <E extends Entity<?>, PE extends Bean & Entity<?>> PresetLookupDao<E>
    newPresetLookupDaoWithChosenDialect(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull PresetCriteriaMaker presetCriteriaMaker,
            @NonNull List<DialectNativeSqlLookup<PE>> nativeSqlLookups,
            @NonNull Class<? extends Dialect> dialectClass
    ) {
        NativeSqlLookup<PE> nativeSqlLookup = nativeSqlLookups.stream().filter(
                lookup -> lookup.supportDialect(dialectClass)
        ).findFirst().orElse(null);
        return newPresetLookupDao(template, entityBeanTransformer, classPE, presetCriteriaMaker, nativeSqlLookup);
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
     * @param nativeSqlLookup       本地 SQL 查询。该值允许为 null。
     * @param <E>                   实体的泛型。
     * @param <PE>                  持久化 Bean 的泛型。
     * @return 新的预设查询数据访问层。
     */
    public static <E extends Entity<?>, PE extends Bean & Entity<?>> PresetLookupDao<E> newPresetLookupDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<E, PE> entityBeanTransformer,
            @NonNull Class<PE> classPE,
            @NonNull PresetCriteriaMaker presetCriteriaMaker,
            @Nullable NativeSqlLookup<PE> nativeSqlLookup
    ) {
        if (Objects.isNull(nativeSqlLookup)) {
            return new HibernatePresetLookupDao<>(template, entityBeanTransformer, classPE, presetCriteriaMaker);
        } else {
            return new HibernateAcceleratePresetLookupDao<>(
                    template, entityBeanTransformer, classPE, nativeSqlLookup, presetCriteriaMaker
            );
        }
    }

    private HibernateDaoFactory() {
        throw new IllegalStateException("禁止实例化");
    }
}
