package com.dwarfeng.subgrade.data.impl.dao.jdbc;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.jdbc.processor.BatchWriteProcessor;
import com.dwarfeng.subgrade.data.sdk.jdbc.processor.SQLAndParameter;
import com.dwarfeng.subgrade.data.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;

/**
 * 使用 Jdbc 实现的 BatchWriteDao。
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcBatchWriteDao<E extends Entity<?>> implements BatchWriteDao<E> {

    private JdbcTemplate template;
    private BatchWriteProcessor<E> processor;

    public JdbcBatchWriteDao(@NotNull JdbcTemplate template, @NotNull BatchWriteProcessor<E> processor) {
        this.template = template;
        this.processor = processor;
    }

    @Override
    public void write(E element) throws DaoException {
        try {
            internalWrite(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalWrite(E element) {
        SQLAndParameter sqlAndParameter = processor.provideWrite(element);
        template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public void batchWrite(List<E> elements) throws DaoException {
        try {
            if (processor.loopWrite()) {
                for (E element : elements) {
                    internalWrite(element);
                }
            } else {
                SQLAndParameter sqlAndParameter = processor.provideBatchWrite(elements);
                if (Objects.nonNull(sqlAndParameter.getParametersList())) {
                    template.batchUpdate(sqlAndParameter.getSql(), sqlAndParameter.getParametersList());
                } else {
                    template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
                }
            }
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

    public BatchWriteProcessor<E> getProcessor() {
        return processor;
    }

    public void setProcessor(@NotNull BatchWriteProcessor<E> processor) {
        this.processor = processor;
    }
}
