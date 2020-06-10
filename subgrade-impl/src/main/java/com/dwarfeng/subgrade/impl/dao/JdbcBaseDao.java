package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.CreateTableTemplate;
import com.dwarfeng.subgrade.sdk.jdbc.template.CrudTemplate;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

/**
 * 使用 Jdbc 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
@SuppressWarnings("DuplicatedCode")
public class JdbcBaseDao<K extends Key, E extends Entity<K>> implements BaseDao<K, E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBaseDao.class);

    private JdbcTemplate jdbcTemplate;
    private CrudTemplate crudTemplate;
    private CrudMapper<K, E> crudMapper;
    private ResultMapper<E> resultMapper;

    public JdbcBaseDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull CrudTemplate crudTemplate,
            @NonNull CrudMapper<K, E> crudMapper,
            @NonNull ResultMapper<E> resultMapper) {
        this(jdbcTemplate, crudTemplate, crudMapper, resultMapper, null);
    }

    public JdbcBaseDao(
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
            String existsSQL = crudTemplate.existsSQL();
            Boolean exists = jdbcTemplate.query(existsSQL, crudMapper.exists2Args(key), ResultSet::next);
            assert exists != null;
            return exists;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            String getSQL = crudTemplate.getSQL();
            return jdbcTemplate.query(getSQL, crudMapper.get2Args(key), rs -> {
                if (rs.next()) {
                    return resultMapper.result2Entity(rs);
                } else {
                    return null;
                }
            });
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
