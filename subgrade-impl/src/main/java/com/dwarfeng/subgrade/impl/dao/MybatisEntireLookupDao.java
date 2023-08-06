package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 使用 MyBatis 实现的 EntireLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisEntireLookupDao<E extends Entity<?>> implements EntireLookupDao<E> {

    public static final String DEFAULT_ENTIRE_LOOKUP_ID = "entireLookup";
    public static final String DEFAULT_ENTIRE_PAGING_ID = "entirePaging";
    public static final String DEFAULT_ENTIRE_COUNT_ID = "entireCount";

    private SqlSessionTemplate template;
    private String namespace;
    private String entireLookupId;
    private String entirePagingId;
    private String entireCountId;

    public MybatisEntireLookupDao(@Nonnull SqlSessionTemplate template, @Nonnull String namespace) {
        this(template, namespace, DEFAULT_ENTIRE_LOOKUP_ID, DEFAULT_ENTIRE_PAGING_ID, DEFAULT_ENTIRE_COUNT_ID);
    }

    public MybatisEntireLookupDao(
            @Nonnull SqlSessionTemplate template,
            @Nonnull String namespace,
            @Nonnull String entireLookupId,
            @Nonnull String entirePagingId,
            @Nonnull String entireCountId) {
        this.template = template;
        this.namespace = namespace;
        this.entireLookupId = entireLookupId;
        this.entirePagingId = entirePagingId;
        this.entireCountId = entireCountId;
    }

    @Override
    public List<E> lookup() throws DaoException {
        try {
            return template.selectList(concatId(entireLookupId));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            return template.selectList(concatId(entireLookupId), pagingInfo);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount() throws DaoException {
        try {
            return template.selectOne(concatId(entireCountId));
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

    public String getEntireLookupId() {
        return entireLookupId;
    }

    public void setEntireLookupId(String entireLookupId) {
        this.entireLookupId = entireLookupId;
    }

    public String getEntirePagingId() {
        return entirePagingId;
    }

    public void setEntirePagingId(String entirePagingId) {
        this.entirePagingId = entirePagingId;
    }

    public String getEntireCountId() {
        return entireCountId;
    }

    public void setEntireCountId(String entireCountId) {
        this.entireCountId = entireCountId;
    }
}
