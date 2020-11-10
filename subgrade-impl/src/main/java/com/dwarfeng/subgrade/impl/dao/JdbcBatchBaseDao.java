package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.BatchBaseProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 Jdbc 实现的 BatchBaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
@SuppressWarnings("DuplicatedCode")
public class JdbcBatchBaseDao<K extends Key, E extends Entity<K>> implements BatchBaseDao<K, E> {

    private JdbcTemplate template;
    private BatchBaseProcessor<K, E> processor;

    public JdbcBatchBaseDao(@NonNull JdbcTemplate template, @NonNull BatchBaseProcessor<K, E> processor) {
        this.template = template;
        this.processor = processor;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            if (Objects.isNull(element.getKey())) {
                throw new UnsupportedOperationException("暂不支持实体对象没有主键/数据表主键自增的情形");
            }
            return internalInsert(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private K internalInsert(E element) {
        SQLAndParameter sqlAndParameter = processor.provideInsert(element);
        template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
        return element.getKey();
    }

    @Override
    public void update(E element) throws DaoException {
        try {
            internalUpdate(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalUpdate(E element) {
        SQLAndParameter sqlAndParameter = processor.provideUpdate(element);
        template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalDelete(K key) {
        SQLAndParameter sqlAndParameter = processor.provideDelete(key);
        template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
    }

    @Override
    public boolean exists(K key) throws DaoException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalExists(K key) {
        SQLAndParameter sqlAndParameter = processor.provideExists(key);
        Boolean flag = template.query(
                sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveExists);
        assert flag != null;
        return flag;
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private E internalGet(K key) {
        SQLAndParameter sqlAndParameter = processor.provideGet(key);
        return template.query(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveGet);
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws DaoException {
        try {
            if (elements.stream().anyMatch(element -> Objects.isNull(element.getKey()))) {
                throw new UnsupportedOperationException("暂不支持实体对象没有主键/数据表主键自增的情形");
            }
            if (processor.loopInsert()) {
                for (E element : elements) {
                    internalInsert(element);
                }
            } else {
                SQLAndParameter sqlAndParameter = processor.provideBatchInsert(elements);
                if (Objects.nonNull(sqlAndParameter.getParametersList())) {
                    template.batchUpdate(sqlAndParameter.getSql(), sqlAndParameter.getParametersList());
                } else {
                    template.update(sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters());
                }
            }
            return elements.stream().map(Entity::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchUpdate(List<E> elements) throws DaoException {
        try {
            if (processor.loopUpdate()) {
                for (E element : elements) {
                    internalUpdate(element);
                }
            } else {
                SQLAndParameter sqlAndParameter = processor.provideBatchUpdate(elements);
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

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            if (processor.loopDelete()) {
                for (K key : keys) {
                    internalDelete(key);
                }
            } else {
                SQLAndParameter sqlAndParameter = processor.provideBatchDelete(keys);
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

    @Override
    public boolean allExists(List<K> keys) throws DaoException {
        try {
            if (processor.loopExists()) {
                for (K key : keys) {
                    if (!internalExists(key)) return false;
                }
                return true;
            } else {
                SQLAndParameter sqlAndParameter = processor.provideAllExists(keys);
                Boolean result = template.query(
                        sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveAllExists);
                assert result != null;
                return result;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean nonExists(List<K> keys) throws DaoException {
        try {
            if (processor.loopExists()) {
                for (K key : keys) {
                    if (internalExists(key)) return false;
                }
                return true;
            } else {
                SQLAndParameter sqlAndParameter = processor.provideNonExists(keys);
                Boolean result = template.query(
                        sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveNonExists);
                assert result != null;
                return result;
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> batchGet(List<K> keys) throws DaoException {
        try {
            if (processor.loopGet()) {
                List<E> elements = new ArrayList<>();
                for (K key : keys) {
                    elements.add(internalGet(key));
                }
                return elements;
            } else {
                SQLAndParameter sqlAndParameter = processor.provideBatchGet(keys);
                return template.query(
                        sqlAndParameter.getSql(), sqlAndParameter.getFirstParameters(), processor::resolveBatchGet);
            }
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

    public BatchBaseProcessor<K, E> getProcessor() {
        return processor;
    }

    public void setProcessor(@NonNull BatchBaseProcessor<K, E> processor) {
        this.processor = processor;
    }
}
