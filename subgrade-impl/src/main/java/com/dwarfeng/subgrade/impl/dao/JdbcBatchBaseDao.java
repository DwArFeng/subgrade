package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.CreateTableTemplate;
import com.dwarfeng.subgrade.sdk.jdbc.template.CrudTemplate;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBatchBaseDao.class);

    private JdbcTemplate jdbcTemplate;
    private CrudTemplate crudTemplate;
    private CrudMapper<K, E> crudMapper;
    private ResultMapper<E> resultMapper;

    public JdbcBatchBaseDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull CrudTemplate crudTemplate,
            @NonNull CrudMapper<K, E> crudMapper,
            @NonNull ResultMapper<E> resultMapper) {
        this(jdbcTemplate, crudTemplate, crudMapper, resultMapper, null);
    }

    public JdbcBatchBaseDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull CrudTemplate crudTemplate,
            @NonNull CrudMapper<K, E> crudMapper,
            @NonNull ResultMapper<E> resultMapper,
            CreateTableTemplate createTableTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.crudTemplate = crudTemplate;
        this.crudMapper = crudMapper;
        this.resultMapper = resultMapper;
        if (Objects.nonNull(createTableTemplate)) {
            creatTable(createTableTemplate);
        }
    }

    private void creatTable(CreateTableTemplate createTableTemplate) {
        List<String> crateTableSQLList = createTableTemplate.createTableSQL();
        for (String crateTableSQL : crateTableSQLList) {
            jdbcTemplate.execute(crateTableSQL);
        }
    }

    @Override
    public K insert(E entity) throws DaoException {
        try {
            String insertSQL = crudTemplate.insertSQL();
            jdbcTemplate.update(insertSQL, crudMapper.insert2Args(entity));
            return entity.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E entity) throws DaoException {
        try {
            String updateSQL = crudTemplate.updateSQL();
            jdbcTemplate.update(updateSQL, crudMapper.update2Args(entity));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            String deleteSQL = crudTemplate.deleteSQL();
            jdbcTemplate.update(deleteSQL, crudMapper.delete2Args(key));
        } catch (Exception e) {
            throw new DaoException(e);
        }
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
        String existsSQL = crudTemplate.existsSQL();
        Boolean exists = jdbcTemplate.query(existsSQL, crudMapper.exists2Args(key), ResultSet::next);
        assert exists != null;
        return exists;
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
        String getSQL = crudTemplate.getSQL();
        return jdbcTemplate.query(getSQL, crudMapper.get2Args(key), rs -> {
            if (rs.next()) {
                return resultMapper.result2Entity(rs);
            } else {
                return null;
            }
        });
    }

    @Override
    public List<K> batchInsert(List<E> entities) throws DaoException {
        try {
            String insertSQL = crudTemplate.insertSQL();
            jdbcTemplate.batchUpdate(insertSQL,
                    entities.stream().map(crudMapper::insert2Args).collect(Collectors.toList()));
            return entities.stream().map(Entity::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchUpdate(List<E> entities) throws DaoException {
        try {
            String updateSQL = crudTemplate.updateSQL();
            jdbcTemplate.batchUpdate(updateSQL,
                    entities.stream().map(crudMapper::update2Args).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            String deleteSQL = crudTemplate.deleteSQL();
            jdbcTemplate.batchUpdate(deleteSQL,
                    keys.stream().map(crudMapper::delete2Args).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean allExists(List<K> keys) throws DaoException {
        try {
            for (K key : keys) {
                if (!internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean nonExists(List<K> keys) throws DaoException {
        try {
            for (K key : keys) {
                if (internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> batchGet(List<K> keys) throws DaoException {
        try {
            List<E> list = new ArrayList<>();
            for (K key : keys) {
                list.add(internalGet(key));
            }
            return list;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(@NonNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CrudTemplate getCrudTemplate() {
        return crudTemplate;
    }

    public void setCrudTemplate(@NonNull CrudTemplate crudTemplate) {
        this.crudTemplate = crudTemplate;
    }

    public CrudMapper<K, E> getCrudMapper() {
        return crudMapper;
    }

    public void setCrudMapper(@NonNull CrudMapper<K, E> crudMapper) {
        this.crudMapper = crudMapper;
    }

    public ResultMapper<E> getResultMapper() {
        return resultMapper;
    }

    public void setResultMapper(@NonNull ResultMapper<E> resultMapper) {
        this.resultMapper = resultMapper;
    }
}
