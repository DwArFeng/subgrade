package com.dwarfeng.subgrade.data.impl.dao.jdbc;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.jdbc.processor.SQLAndParameter;
import com.dwarfeng.subgrade.data.sdk.jdbc.processor.WriteProcessor;
import com.dwarfeng.subgrade.data.stack.dao.WriteDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 使用 Jdbc 实现的 WriteDao。
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcWriteDao<E extends Entity<?>> implements WriteDao<E> {

    private JdbcTemplate template;
    private WriteProcessor<E> processor;

    public JdbcWriteDao(@NotNull JdbcTemplate template, @NotNull WriteProcessor<E> processor) {
        this.template = template;
        this.processor = processor;
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public void write(E element) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideWrite(element);
            template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull JdbcTemplate template) {
        this.template = template;
    }

    public WriteProcessor<E> getProcessor() {
        return processor;
    }

    public void setProcessor(@NotNull WriteProcessor<E> processor) {
        this.processor = processor;
    }
}
