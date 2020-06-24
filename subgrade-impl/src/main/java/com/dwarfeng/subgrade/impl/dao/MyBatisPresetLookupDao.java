package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.mybatis.PresetLookupInfo;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 使用 MyBatis 实现的 PresetLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MyBatisPresetLookupDao<E extends Entity<?>> implements PresetLookupDao<E> {

    public static final String DEFAULT_PRESET_LOOKUP_ID = "presetLookup";
    public static final String DEFAULT_PRESET_PAGING_ID = "presetPaging";
    public static final String DEFAULT_PRESET_COUNT_ID = "presetCount";

    private SqlSessionTemplate template;
    private String namespace;
    private String presetLookupId;
    private String presetPagingId;
    private String presetCountId;

    public MyBatisPresetLookupDao(@NonNull SqlSessionTemplate template, @NonNull String namespace) {
        this(template, namespace, DEFAULT_PRESET_LOOKUP_ID, DEFAULT_PRESET_PAGING_ID, DEFAULT_PRESET_COUNT_ID);
    }

    public MyBatisPresetLookupDao(
            @NonNull SqlSessionTemplate template,
            @NonNull String namespace,
            @NonNull String presetLookupId,
            @NonNull String presetPagingId,
            @NonNull String presetCountId) {
        this.template = template;
        this.namespace = namespace;
        this.presetLookupId = presetLookupId;
        this.presetPagingId = presetPagingId;
        this.presetCountId = presetCountId;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            return template.selectList(concatId(presetLookupId), new PresetLookupInfo(preset, objs, null));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            return template.selectList(concatId(presetPagingId), new PresetLookupInfo(preset, objs, pagingInfo));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            return template.selectOne(concatId(presetCountId), new PresetLookupInfo(preset, objs, null));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private String concatId(String id) {
        return namespace + '.' + id;
    }

    public SqlSessionTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SqlSessionTemplate template) {
        this.template = template;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPresetLookupId() {
        return presetLookupId;
    }

    public void setPresetLookupId(String presetLookupId) {
        this.presetLookupId = presetLookupId;
    }

    public String getPresetPagingId() {
        return presetPagingId;
    }

    public void setPresetPagingId(String presetPagingId) {
        this.presetPagingId = presetPagingId;
    }

    public String getPresetCountId() {
        return presetCountId;
    }

    public void setPresetCountId(String presetCountId) {
        this.presetCountId = presetCountId;
    }
}
