package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 通用整体查询模板。
 *
 * <p>
 * 整体查询模板的通用抽象实现。
 * <p>
 * 该类是为了继承而设计的，继承该类，并重写该类的内部方法，可以实现不同数据库的整体查询模板。<br>
 * 重写该类时，请重写该类的内部方法，如 {@link #internalLookupSQL()}，而不是重写直接继承的方法 {@link #lookupSQL()}，
 * 其它方法同理。
 * <p>
 * 该类设计了样板缓存，如果数据访问层多次调用该模板的某一个方法，该模板会利用缓存的内容进行加速；
 * 对于极少数的，每次调用方法返回不同 SQL 的模板，应该将缓存机制禁止。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class GeneralEntireLookupTemplate implements EntireLookupTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralEntireLookupTemplate.class);

    protected TableDefinition tableDefinition;
    protected boolean caching;

    private final SQLCache sqlCache = new SQLCache();

    public GeneralEntireLookupTemplate(@NonNull TableDefinition tableDefinition) {
        this(tableDefinition, true);
    }

    public GeneralEntireLookupTemplate(@NonNull TableDefinition tableDefinition, boolean caching) {
        this.tableDefinition = tableDefinition;
        this.caching = caching;
    }

    @Override
    public String lookupSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getLookupSQL())) {
            sql = sqlCache.getLookupSQL();
            LOGGER.debug("lookupSQL (cached): " + sql);
        } else {
            sql = internalLookupSQL();
            if (caching) {
                sqlCache.setLookupSQL(sql);
            }
            LOGGER.debug("lookupSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部查询的 SQL 语句。
     *
     * @return 内部查询的 SQL 语句。
     */
    protected abstract String internalLookupSQL();

    @Override
    public String pagingSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getPagingSQL())) {
            sql = sqlCache.getPagingSQL();
            LOGGER.debug("pagingSQL (cached): " + sql);
        } else {
            sql = internalPagingSQL();
            if (caching) {
                sqlCache.setPagingSQL(sql);
            }
            LOGGER.debug("pagingSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部分页的 SQL 语句。
     *
     * @return 内部分页的 SQL 语句。
     */
    protected abstract String internalPagingSQL();

    @Override
    public String countSQL() {
        String sql;
        if (caching && Objects.nonNull(sqlCache.getCountSQL())) {
            sql = sqlCache.getCountSQL();
            LOGGER.debug("countSQL (cached): " + sql);
        } else {
            sql = internalCountSQL();
            if (caching) {
                sqlCache.setCountSQL(sql);
            }
            LOGGER.debug("countSQL: " + sql);
        }
        return sql;
    }

    /**
     * 获取内部计数的 SQL 语句。
     *
     * @return 内部计数的 SQL 语句。
     */
    protected abstract String internalCountSQL();

    /**
     * 清除SQL缓存。
     */
    protected void clearCache() {
        sqlCache.setLookupSQL(null);
        sqlCache.setPagingSQL(null);
        sqlCache.setCountSQL(null);
    }

    @Override
    public String toString() {
        return "GeneralEntireLookupTemplate{" +
                "tableDefinition=" + tableDefinition +
                '}';
    }

    private static final class SQLCache {

        private String lookupSQL;
        private String pagingSQL;
        private String countSQL;

        public String getLookupSQL() {
            return lookupSQL;
        }

        public void setLookupSQL(String lookupSQL) {
            this.lookupSQL = lookupSQL;
        }

        public String getPagingSQL() {
            return pagingSQL;
        }

        public void setPagingSQL(String pagingSQL) {
            this.pagingSQL = pagingSQL;
        }

        public String getCountSQL() {
            return countSQL;
        }

        public void setCountSQL(String countSQL) {
            this.countSQL = countSQL;
        }

        @Override
        public String toString() {
            return "SQLCache{" +
                    "lookupSQL='" + lookupSQL + '\'' +
                    ", pagingSQL='" + pagingSQL + '\'' +
                    ", countSQL='" + countSQL + '\'' +
                    '}';
        }
    }
}
