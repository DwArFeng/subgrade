package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.data.sdk.hibernate.nativelookup.NativeLookup;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 支持本地查询加速的 Hibernate 预设查询数据访问层。
 *
 * <p>
 * 当本地查询实现支持指定预设时，该类直接使用 JDBC 连接执行本地查询；否则回退到 Jakarta Criteria 查询。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class HibernateAccelerablePresetLookupDao<E extends Entity<?>, PE extends Bean> implements PresetLookupDao<E> {

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<E, PE> entityBeanTransformer;
    @NotNull
    private Class<PE> classPE;
    @NotNull
    private NativeLookup<E> nativeLookup;
    @NotNull
    private PresetCriteriaMaker<PE> presetCriteriaMaker;

    public HibernateAccelerablePresetLookupDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<E, PE> entityBeanTransformer,
            @NotNull Class<PE> classPE,
            @NotNull NativeLookup<E> nativeLookup,
            @NotNull PresetCriteriaMaker<PE> presetCriteriaMaker
    ) {
        this.template = template;
        this.entityBeanTransformer = entityBeanTransformer;
        this.classPE = classPE;
        this.nativeLookup = nativeLookup;
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        if (!nativeLookup.supportPreset(preset)) {
            return fallbackDao().lookup(preset, objs);
        }
        try {
            return template.executeWithNativeSession(session -> session.doReturningWork(
                    connection -> nativeLookup.lookupEntity(connection, preset, objs)
            ));
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        if (!nativeLookup.supportPreset(preset)) {
            return fallbackDao().lookup(preset, objs, pagingInfo);
        }
        try {
            return template.executeWithNativeSession(session -> session.doReturningWork(
                    connection -> nativeLookup.lookupEntity(connection, preset, objs, pagingInfo)
            ));
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        if (!nativeLookup.supportPreset(preset)) {
            return fallbackDao().lookupCount(preset, objs);
        }
        try {
            return template.executeWithNativeSession(session -> session.doReturningWork(
                    connection -> nativeLookup.lookupCount(connection, preset, objs)
            ));
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        if (!nativeLookup.supportPreset(preset)) {
            return fallbackDao().lookupFirst(preset, objs);
        }
        try {
            List<E> result = template.executeWithNativeSession(session -> session.doReturningWork(
                    connection -> nativeLookup.lookupEntity(connection, preset, objs, PagingInfo.FIRST_ONE)
            ));
            return result.stream().findFirst().orElse(null);
        } catch (Exception exception) {
            throw new DaoException(exception);
        }
    }

    private HibernatePresetLookupDao<E, PE> fallbackDao() {
        return new HibernatePresetLookupDao<>(template, entityBeanTransformer, classPE, presetCriteriaMaker);
    }

    @NotNull
    public HibernateOperations getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull HibernateOperations template) {
        this.template = template;
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
    public NativeLookup<E> getNativeLookup() {
        return nativeLookup;
    }

    public void setNativeLookup(@NotNull NativeLookup<E> nativeLookup) {
        this.nativeLookup = nativeLookup;
    }

    @NotNull
    public PresetCriteriaMaker<PE> getPresetCriteriaMaker() {
        return presetCriteriaMaker;
    }

    public void setPresetCriteriaMaker(@NotNull PresetCriteriaMaker<PE> presetCriteriaMaker) {
        this.presetCriteriaMaker = presetCriteriaMaker;
    }

    @Override
    public String toString() {
        return "HibernateAccelerablePresetLookupDao{" +
                "template=" + template +
                ", entityBeanTransformer=" + entityBeanTransformer +
                ", classPE=" + classPE +
                ", nativeLookup=" + nativeLookup +
                ", presetCriteriaMaker=" + presetCriteriaMaker +
                '}';
    }
}
