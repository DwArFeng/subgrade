package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.processor.BaseProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.processor.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 使用 Jdbc 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class JdbcBaseDao<K extends Key, E extends Entity<K>> implements BaseDao<K, E> {

    private JdbcTemplate template;
    private BaseProcessor<K, E> processor;

    public JdbcBaseDao(@Nonnull JdbcTemplate template, @Nonnull BaseProcessor<K, E> processor) {
        this.template = template;
        this.processor = processor;
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public K insert(E element) throws DaoException {
        try {
            if (Objects.isNull(element.getKey())) {
                throw new UnsupportedOperationException("暂不支持实体对象没有主键/数据表主键自增的情形");
            }
            SQLAndParameter sqlAndParameter = processor.provideInsert(element);
            template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
            return element.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public void update(E element) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideUpdate(element);
            template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public void delete(K key) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideDelete(key);
            template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public boolean exists(K key) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideExists(key);
            Boolean flag = template.query(
                    sqlAndParameter.getSql(),
                    new ArgumentPreparedStatementSetter(sqlAndParameter.getFirstParameters()),
                    processor::resolveExists
            );
            assert flag != null;
            return flag;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    // SQL 的安全性由 BaseProcessor 保证。
    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public E get(K key) throws DaoException {
        try {
            SQLAndParameter sqlAndParameter = processor.provideGet(key);
            return template.query(
                    sqlAndParameter.getSql(),
                    new ArgumentPreparedStatementSetter(sqlAndParameter.getFirstParameters()),
                    processor::resolveGet
            );
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

    public BaseProcessor<K, E> getProcessor() {
        return processor;
    }

    public void setProcessor(@Nonnull BaseProcessor<K, E> processor) {
        this.processor = processor;
    }
}
