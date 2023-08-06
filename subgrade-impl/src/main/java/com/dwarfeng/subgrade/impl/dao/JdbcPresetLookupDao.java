package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.processor.PresetLookupProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.processor.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 使用 Jdbc 实现的 PresetLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcPresetLookupDao<E extends Entity<?>> implements PresetLookupDao<E> {

    private JdbcTemplate template;
    private PresetLookupProcessor<E> processor;

    public JdbcPresetLookupDao(@Nonnull JdbcTemplate template, @Nonnull PresetLookupProcessor<E> processor) {
        this.template = template;
        this.processor = processor;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.providePresetLookup(preset, objs);
            return template.query(
                    sqlAndParameter.getSql(),
                    new ArgumentPreparedStatementSetter(sqlAndParameter.getFirstParameters()),
                    processor::resolvePresetLookup
            );
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.providePresetPaging(preset, objs, pagingInfo);
            return template.query(
                    sqlAndParameter.getSql(),
                    new ArgumentPreparedStatementSetter(sqlAndParameter.getFirstParameters()),
                    processor::resolvePresetPaging
            );
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.providePresetCount(preset, objs);
            Integer result = template.query(
                    sqlAndParameter.getSql(),
                    new ArgumentPreparedStatementSetter(sqlAndParameter.getFirstParameters()),
                    processor::resolvePresetCount
            );
            assert result != null;
            return result;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull JdbcTemplate template) {
        this.template = template;
    }

    public PresetLookupProcessor<E> getProcessor() {
        return processor;
    }

    public void setProcessor(@Nonnull PresetLookupProcessor<E> processor) {
        this.processor = processor;
    }
}
