package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.processor.EntireLookupProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.processor.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 使用 Jdbc 实现的 EntireLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcEntireLookupDao<E extends Entity<?>> implements EntireLookupDao<E> {

    private JdbcTemplate template;
    private EntireLookupProcessor<E> processor;

    public JdbcEntireLookupDao(@NonNull JdbcTemplate template, @NonNull EntireLookupProcessor<E> processor) {
        this.template = template;
        this.processor = processor;
    }

    @Override
    public List<E> lookup() throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideEntireLookup();
            return template.query(
                    sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveEntireLookup);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideEntirePaging(pagingInfo);
            return template.query(
                    sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveEntirePaging);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount() throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideEntireCount();
            Integer result = template.query(
                    sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveEntireCount);
            assert result != null;
            return result;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull JdbcTemplate template) {
        this.template = template;
    }

    public EntireLookupProcessor<E> getProcessor() {
        return processor;
    }

    public void setProcessor(@NonNull EntireLookupProcessor<E> processor) {
        this.processor = processor;
    }
}
