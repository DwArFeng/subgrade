package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 通用Crud模板。
 *
 * <p>
 * Crud模板的通用抽象实现。
 * <p>
 * 该类是为了继承而设计的，继承该类，并重写该类的内部方法，可以实现不同数据库的 Crud 模板。<br>
 * 重写该类时，请重写该类的内部方法，如 {@link #internalInsertSQL()}，而不是重写直接继承的方法 {@link #insertSQL()}，
 * 其它方法同理。
 * <p>
 * 该类设计了样板缓存，如果数据访问层多次调用该模板的某一个方法，该模板会利用缓存的内容进行加速；
 * 对于极少数的，每次调用方法返回不同 SQL 的模板，应该将缓存机制禁止。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class GeneralCrudTemplate implements CrudTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralCrudTemplate.class);

    protected TableDefinition tableDefinition;
    protected boolean caching;

    private final SQLCache sqlCache = new SQLCache();

    public GeneralCrudTemplate(@NonNull TableDefinition tableDefinition) {
        this(tableDefinition, true);
    }

    public GeneralCrudTemplate(@NonNull TableDefinition tableDefinition, boolean caching) {
        this.tableDefinition = tableDefinition;
        this.caching = caching;
    }

    @Override
    public String insertSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getInsertSQL())) {
            sql = sqlCache.getInsertSQL();
            LOGGER.debug("insertSQL (cached): " + sql);
        } else {
            sql = internalInsertSQL();
            if (caching) {
                sqlCache.setInsertSQL(sql);
            }
            LOGGER.debug("insertSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部的插入SQL语句。
     *
     * @return 内部的插入SQL语句。
     */
    protected abstract String internalInsertSQL();

    @Override
    public String updateSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getUpdateSQL())) {
            sql = sqlCache.getUpdateSQL();
            LOGGER.debug("updateSQL (cached): " + sql);
        } else {
            sql = internalUpdateSQL();
            if (caching) {
                sqlCache.setUpdateSQL(sql);
            }
            LOGGER.debug("updateSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部的更新SQL语句。
     *
     * @return 内部的更新SQL语句。
     */
    protected abstract String internalUpdateSQL();

    @Override
    public String deleteSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getDeleteSQL())) {
            sql = sqlCache.getDeleteSQL();
            LOGGER.debug("deleteSQL (cached): " + sql);
        } else {
            sql = internalDeleteSQL();
            if (caching) {
                sqlCache.setDeleteSQL(sql);
            }
            LOGGER.debug("deleteSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部的删除SQL语句。
     *
     * @return 内部的删除SQL语句。
     */
    protected abstract String internalDeleteSQL();

    @Override
    public String getSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getGetSQL())) {
            sql = sqlCache.getGetSQL();
            LOGGER.debug("getSQL (cached): " + sql);
        } else {
            sql = internalGetSQL();
            if (caching) {
                sqlCache.setGetSQL(sql);
            }
            LOGGER.debug("getSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部的获取SQL语句。
     *
     * @return 内部的获取SQL语句。
     */
    protected abstract String internalGetSQL();

    @Override
    public String existsSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getExistsSQL())) {
            sql = sqlCache.getExistsSQL();
            LOGGER.debug("existsSQL (cached): " + sql);
        } else {
            sql = internalExistsSQL();
            if (caching) {
                sqlCache.setExistsSQL(sql);
            }
            LOGGER.debug("existsSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部的存在SQL语句。
     *
     * @return 内部的存在SQL语句。
     */
    protected abstract String internalExistsSQL();

    /**
     * 清除SQL缓存。
     */
    protected void clearCache() {
        sqlCache.setInsertSQL(null);
        sqlCache.setUpdateSQL(null);
        sqlCache.setDeleteSQL(null);
        sqlCache.setExistsSQL(null);
        sqlCache.setGetSQL(null);
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public boolean isCaching() {
        return caching;
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

    @Override
    public String toString() {
        return "GeneralCrudTemplate{" +
                "tableDefinition=" + tableDefinition +
                ", caching=" + caching +
                '}';
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
