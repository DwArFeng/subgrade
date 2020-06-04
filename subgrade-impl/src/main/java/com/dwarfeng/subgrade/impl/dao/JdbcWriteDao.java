package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.template.EntityMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.SQLProvider;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.WriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * 使用 Jdbc 实现的 WriteDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
@SuppressWarnings("DuplicatedCode")
public class JdbcWriteDao<E extends Entity<?>> implements WriteDao<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBatchBaseDao.class);

    private JdbcTemplate jdbcTemplate;
    private SQLProvider sqlProvider;
    private EntityMapper<?, E> entityMapper;

    private final SQLCache sqlCache = new SQLCache();

    public JdbcWriteDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull SQLProvider sqlProvider,
            @NonNull EntityMapper<?, E> entityMapper) {
        this(jdbcTemplate, sqlProvider, entityMapper, false);
    }

    public JdbcWriteDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull SQLProvider sqlProvider,
            @NonNull EntityMapper<?, E> entityMapper,
            boolean createTable) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlProvider = sqlProvider;
        this.entityMapper = entityMapper;
        if (createTable) {
            creatTable();
        }
    }

    private void creatTable() {
        List<String> crateTableSQLs;
        try {
            crateTableSQLs = sqlProvider.provideCreateTableSQL();
        } catch (UnsupportedOperationException e) {
            LOGGER.warn("指定的 BaseSqlProvider 不支持建表，将不会执行建表语句");
            return;
        }
        for (String crateTableSQL : crateTableSQLs) {
            jdbcTemplate.execute(crateTableSQL);
        }
    }

    @Override
    public void write(E entity) throws DaoException {
        try {
            String insertSQL = sqlCache.getInsertSQL();
            if (Objects.isNull(insertSQL)) {
                insertSQL = sqlProvider.provideInsertSQL();
                sqlCache.setInsertSQL(insertSQL);
            }
            jdbcTemplate.update(insertSQL, entityMapper.entity2Objects(entity));
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

    public SQLProvider getSqlProvider() {
        return sqlProvider;
    }

    public void setSqlProvider(@NonNull SQLProvider sqlProvider) {
        this.sqlProvider = sqlProvider;
    }

    public EntityMapper<?, E> getEntityMapper() {
        return entityMapper;
    }

    public void setEntityMapper(@NonNull EntityMapper<?, E> entityMapper) {
        this.entityMapper = entityMapper;
    }

    private static final class SQLCache {

        private String insertSQL;

        public String getInsertSQL() {
            return insertSQL;
        }

        public void setInsertSQL(String insertSQL) {
            this.insertSQL = insertSQL;
        }

        @Override
        public String toString() {
            return "SQLCache{" +
                    "insertSQL='" + insertSQL + '\'' +
                    '}';
        }
    }
}
