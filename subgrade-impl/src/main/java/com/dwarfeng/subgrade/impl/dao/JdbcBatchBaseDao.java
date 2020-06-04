package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.jdbc.template.EntityMapper;
import com.dwarfeng.subgrade.sdk.jdbc.template.SQLProvider;
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
    private SQLProvider sqlProvider;
    private EntityMapper<K, E> entityMapper;

    private final SQLCache sqlCache = new SQLCache();

    public JdbcBatchBaseDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull SQLProvider sqlProvider,
            @NonNull EntityMapper<K, E> entityMapper) {
        this(jdbcTemplate, sqlProvider, entityMapper, false);
    }

    public JdbcBatchBaseDao(
            @NonNull JdbcTemplate jdbcTemplate,
            @NonNull SQLProvider sqlProvider,
            @NonNull EntityMapper<K, E> entityMapper,
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
    public K insert(E entity) throws DaoException {
        try {
            String insertSQL = sqlCache.getInsertSQL();
            if (Objects.isNull(insertSQL)) {
                insertSQL = sqlProvider.provideInsertSQL();
                sqlCache.setInsertSQL(insertSQL);
            }
            jdbcTemplate.update(insertSQL, entityMapper.entity2Objects(entity));
            return entity.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E entity) throws DaoException {
        try {
            String updateSQL = sqlCache.getUpdateSQL();
            if (Objects.isNull(updateSQL)) {
                updateSQL = sqlProvider.provideUpdateSQL();
                sqlCache.setUpdateSQL(updateSQL);
            }
            jdbcTemplate.update(updateSQL, entityMapper.entity2Objects(entity));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            String deleteSQL = sqlCache.getDeleteSQL();
            if (Objects.isNull(deleteSQL)) {
                deleteSQL = sqlProvider.provideDeleteSQL();
                sqlCache.setDeleteSQL(deleteSQL);
            }
            jdbcTemplate.update(deleteSQL, entityMapper.key2Objects(key));
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
        String existsSQL = sqlCache.getExistsSQL();
        if (Objects.isNull(existsSQL)) {
            existsSQL = sqlProvider.provideExistsSQL();
            sqlCache.setExistsSQL(existsSQL);
        }
        Boolean exists = jdbcTemplate.query(existsSQL, entityMapper.key2Objects(key), ResultSet::next);
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
        String getSQL = sqlCache.getGetSQL();
        if (Objects.isNull(getSQL)) {
            getSQL = sqlProvider.provideGetSQL();
            sqlCache.setGetSQL(getSQL);
        }
        return jdbcTemplate.query(getSQL, entityMapper.key2Objects(key), rs -> {
            if (rs.next()) {
                Object[] objects = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 0; i < objects.length; i++) {
                    objects[i] = rs.getObject(i);
                }
                return entityMapper.objects2Entity(objects);
            } else {
                return null;
            }
        });
    }

    @Override
    public List<K> batchInsert(List<E> entities) throws DaoException {
        try {
            String insertSQL = sqlCache.getInsertSQL();
            if (Objects.isNull(insertSQL)) {
                insertSQL = sqlProvider.provideInsertSQL();
                sqlCache.setInsertSQL(insertSQL);
            }
            jdbcTemplate.batchUpdate(insertSQL,
                    entities.stream().map(entityMapper::entity2Objects).collect(Collectors.toList()));
            return entities.stream().map(Entity::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchUpdate(List<E> entities) throws DaoException {
        try {
            String updateSQL = sqlCache.getUpdateSQL();
            if (Objects.isNull(updateSQL)) {
                updateSQL = sqlProvider.provideUpdateSQL();
                sqlCache.setUpdateSQL(updateSQL);
            }
            jdbcTemplate.batchUpdate(updateSQL,
                    entities.stream().map(entityMapper::entity2Objects).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            String deleteSQL = sqlCache.getDeleteSQL();
            if (Objects.isNull(deleteSQL)) {
                deleteSQL = sqlProvider.provideDeleteSQL();
                sqlCache.setDeleteSQL(deleteSQL);
            }
            jdbcTemplate.batchUpdate(deleteSQL,
                    keys.stream().map(entityMapper::key2Objects).collect(Collectors.toList()));
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

    public SQLProvider getSqlProvider() {
        return sqlProvider;
    }

    public void setSqlProvider(SQLProvider sqlProvider) {
        this.sqlProvider = sqlProvider;
    }

    public EntityMapper<K, E> getEntityMapper() {
        return entityMapper;
    }

    public void setEntityMapper(@NonNull EntityMapper<K, E> entityMapper) {
        this.entityMapper = entityMapper;
    }

    private static final class SQLCache {

        private String insertSQL;
        private String updateSQL;
        private String deleteSQL;
        private String existsSQL;
        private String getSQL;

        public String getInsertSQL() {
            return insertSQL;
        }

        public void setInsertSQL(String insertSQL) {
            this.insertSQL = insertSQL;
        }

        public String getUpdateSQL() {
            return updateSQL;
        }

        public void setUpdateSQL(String updateSQL) {
            this.updateSQL = updateSQL;
        }

        public String getDeleteSQL() {
            return deleteSQL;
        }

        public void setDeleteSQL(String deleteSQL) {
            this.deleteSQL = deleteSQL;
        }

        public String getExistsSQL() {
            return existsSQL;
        }

        public void setExistsSQL(String existsSQL) {
            this.existsSQL = existsSQL;
        }

        public String getGetSQL() {
            return getSQL;
        }

        public void setGetSQL(String getSQL) {
            this.getSQL = getSQL;
        }

        @Override
        public String toString() {
            return "SQLCache{" +
                    "insertSQL='" + insertSQL + '\'' +
                    ", updateSQL='" + updateSQL + '\'' +
                    ", deleteSQL='" + deleteSQL + '\'' +
                    ", existsSQL='" + existsSQL + '\'' +
                    ", getSQL='" + getSQL + '\'' +
                    '}';
        }
    }
}
