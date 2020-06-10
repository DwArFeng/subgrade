package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.CreateTableTemplate;
import com.dwarfeng.subgrade.sdk.jdbc.template.CrudTemplate;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 Jdbc 实现的 BatchWriteDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
@SuppressWarnings("DuplicatedCode")
public class JdbcBatchWriteDao<E extends Entity<?>> implements BatchWriteDao<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBatchBaseDao.class);

    private JdbcTemplate jdbcTemplate;
    private CrudTemplate crudTemplate;
    private CrudMapper<?, E> crudMapper;

    public JdbcBatchWriteDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull CrudTemplate crudTemplate,
            @NonNull CrudMapper<?, E> crudMapper) {
        this(jdbcTemplate, crudTemplate, crudMapper, null);
    }

    public JdbcBatchWriteDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull CrudTemplate crudTemplate,
            @NonNull CrudMapper<?, E> crudMapper,
            CreateTableTemplate createTableTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.crudTemplate = crudTemplate;
        this.crudMapper = crudMapper;
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
    public void write(E entity) throws DaoException {
        try {
            String insertSQL = crudTemplate.insertSQL();
            jdbcTemplate.update(insertSQL, crudMapper.insert2Args(entity));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchWrite(List<E> entities) throws DaoException {
        try {
            String insertSQL = crudTemplate.insertSQL();
            jdbcTemplate.batchUpdate(insertSQL,
                    entities.stream().map(crudMapper::insert2Args).collect(Collectors.toList()));
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

    public CrudMapper<?, E> getCrudMapper() {
        return crudMapper;
    }

    public void setCrudMapper(@NonNull CrudMapper<?, E> crudMapper) {
        this.crudMapper = crudMapper;
    }
}
