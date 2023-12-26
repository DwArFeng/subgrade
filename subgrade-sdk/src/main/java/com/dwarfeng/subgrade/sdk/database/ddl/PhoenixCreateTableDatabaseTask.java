package com.dwarfeng.subgrade.sdk.database.ddl;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.OptionalDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper.IndexAsyncType;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper.UpdateCacheFrequencyInfo;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Phoenix 建表数据库任务。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
@SuppressWarnings("DuplicatedCode")
public class PhoenixCreateTableDatabaseTask implements DatabaseTask<Object> {

    private static final String CACHE_SQL_CREATE_TABLE = "CACHE_SQL_CREATE_TABLE";

    private TableDefinition tableDefinition;

    public PhoenixCreateTableDatabaseTask(@Nonnull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public Object todo(Connection connection) throws Exception {
        @SuppressWarnings("unchecked")
        List<String> sqlList = (List<String>) tableDefinition.getOrPutCache(
                CACHE_SQL_CREATE_TABLE, provideCreateTableSQL());
        for (String sql : sqlList) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }
        return null;
    }

    private List<String> provideCreateTableSQL() {
        List<String> sqlList = new ArrayList<>();
        sqlList.add(createTableSQL());
        List<OptionalDefinition> indexes = PhoenixHelper.getIndexes(tableDefinition);
        for (OptionalDefinition index : indexes) {
            sqlList.add(createIndexSQL(index));
        }
        return sqlList;
    }

    private String createTableSQL() {
        StringBuilder sb = new StringBuilder();
        // 追加 CREATE TABLE IF NOT EXISTS + 表名
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(PhoenixHelper.getFullTableName(tableDefinition)).append(" (");
        // 追加全列序列。
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        for (int i = 0; i < columnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            // 追加列的名称和类型。
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(columnDefinition.getName()).append(' ').append(columnDefinition.getType());
            // 追加 NULL 或者 NOT NULL
            PhoenixHelper.ColumnNullable columnNullable = PhoenixHelper.getColumnNullable(columnDefinition);
            if (Objects.nonNull(columnNullable) && !columnNullable.sqlFragment().isEmpty()) {
                sb.append(' ').append(columnNullable.sqlFragment());
            }
            String defaultValue = PhoenixHelper.getColumnDefaultValue(columnDefinition);
            // 追加 DEFAULT
            if (Objects.nonNull(defaultValue)) {
                sb.append(' ').append(defaultValue);
            }
        }
        // 追加主键约束。
        sb.append(", CONSTRAINT pk PRIMARY KEY (");
        Set<String> ascColumnNames = PhoenixHelper.getPrimaryKeyAscColumnNames(tableDefinition);
        Set<String> descColumnNames = PhoenixHelper.getPrimaryKeyDescColumnNames(tableDefinition);
        String rowTimestampColumnName = PhoenixHelper.getPrimaryKeyRowTimestampColumn(tableDefinition);
        List<ColumnDefinition> primaryKeyColumns = PhoenixHelper.getPrimaryKeyColumns(tableDefinition);
        // 追加主键序列。
        for (int i = 0; i < primaryKeyColumns.size(); i++) {
            ColumnDefinition columnDefinition = primaryKeyColumns.get(i);
            String columnName = columnDefinition.getName();
            // 追加列的名称和类型。
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(columnName);
            // 追加 ASC 或 DESC。
            if (ascColumnNames.contains(columnName)) {
                sb.append(" ASC");
            } else if (descColumnNames.contains(columnName)) {
                sb.append(" DESC");
            }
            // 追加 ROW_TIMESTAMP。
            if (Objects.equals(rowTimestampColumnName, columnName)) {
                sb.append(" ROW_TIMESTAMP");
            }
        }
        sb.append("))");
        // 追加数据表的选项。
        List<String> options = new ArrayList<>();
        Object val = PhoenixHelper.getTableSaltBuckets(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("SALT_BUCKETS=%d", val));
        }
        val = PhoenixHelper.getTableDisableWal(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("DISABLE_WAL=%b", val));
        }
        val = PhoenixHelper.getTableImmutableRows(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("IMMUTABLE_ROWS=%b", val));
        }
        val = PhoenixHelper.getTableMultiTelnet(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("MULTI_TELNET=%b", val));
        }
        val = PhoenixHelper.getTableDefaultColumnFamily(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("DEFAULT_COLUMN_FAMILY='%s'", val));
        }
        val = PhoenixHelper.getTableStoreNulls(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("STORE_NULLS=%b", val));
        }
        val = PhoenixHelper.getTableTransactional(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("TRANSACTIONAL=%b", val));
        }
        val = PhoenixHelper.getTableUpdateCacheFrequency(tableDefinition);
        if (Objects.nonNull(val)) {
            UpdateCacheFrequencyInfo updateCacheFrequencyInfo = (UpdateCacheFrequencyInfo) val;
            if (updateCacheFrequencyInfo.isMatchLong()) {
                options.add(String.format("UPDATE_CACHE_FREQUENCY=%d", updateCacheFrequencyInfo.getLongValue()));
            } else if (updateCacheFrequencyInfo.isMatchUpdateCacheFrequency()) {
                options.add(String.format("UPDATE_CACHE_FREQUENCY='%s'",
                        updateCacheFrequencyInfo.getUpdateCacheFrequencyValue().sqlFragment()));
            }
        }
        val = PhoenixHelper.getTableAppendOnlySchema(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("APPEND_ONLY_SCHEMA=%b", val));
        }
        val = PhoenixHelper.getTableAutoPartitionSeq(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("AUTO_PARTITION_SEQ='%s'", val));
        }
        val = PhoenixHelper.getTableGuidPostsWidth(tableDefinition);
        if (Objects.nonNull(val)) {
            options.add(String.format("GUIDE_POSTS_WIDTH=%d", val));
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == 0) {
                sb.append(' ');
            } else {
                sb.append(", ");
            }
            sb.append(options.get(i));
        }
        // 追加 SPLIT ON。
        List<String> splitOnColumns = PhoenixHelper.getTableSplitPoint(tableDefinition);
        if (!splitOnColumns.isEmpty()) {
            sb.append(" SPLIT ON (");
            for (int i = 0; i < splitOnColumns.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(splitOnColumns.get(i));
            }
            sb.append(')');
        }
        return sb.toString();
    }

    private String createIndexSQL(OptionalDefinition index) {
        StringBuilder sb = new StringBuilder();
        // 追加 CREATE。
        sb.append("CREATE");
        PhoenixHelper.IndexType indexType = PhoenixHelper.getIndexType(index);
        // 按需追加 LOCAL
        if (Objects.nonNull(indexType) && !indexType.sqlFragment().isEmpty()) {
            sb.append(' ').append(indexType.sqlFragment());
        }
        // 追加 INDEX IF NOT EXISTS + index名称 + ON + 表名
        sb.append(" INDEX IF NOT EXISTS ").append(index.getName()).append(" ON ")
                .append(PhoenixHelper.getFullTableName(tableDefinition)).append(" (");
        // 追加索引列。
        List<String> indexContextColumnNames = PhoenixHelper.getIndexContextColumnNames(index);
        Set<String> ascColumnNames = PhoenixHelper.getIndexAscColumnNames(index);
        Set<String> descColumnNames = PhoenixHelper.getIndexDescColumnNames(index);
        for (int i = 0; i < indexContextColumnNames.size(); i++) {
            String columnName = indexContextColumnNames.get(i);
            // 追加列的名称。
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(columnName);
            // 追加 ASC 或 DESC。
            if (ascColumnNames.contains(columnName)) {
                sb.append(" ASC");
            } else if (descColumnNames.contains(columnName)) {
                sb.append(" DESC");
            }
        }
        sb.append(')');
        // 追加 INCLUDE。
        List<String> includeColumnNames = PhoenixHelper.getIndexIncludeColumnNames(index);
        if (!includeColumnNames.isEmpty()) {
            sb.append(" INCLUDE (");
            for (int i = 0; i < includeColumnNames.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(includeColumnNames.get(i));
            }
            sb.append(')');
        }
        // 按需增加 ASYNC。
        IndexAsyncType indexAsyncType = PhoenixHelper.getIndexAsyncType(index);
        if (Objects.nonNull(indexAsyncType) && !indexAsyncType.sqlFragment().isEmpty()) {
            sb.append(' ').append(indexAsyncType.sqlFragment());
        }
        // 追加索引的选项。
        List<String> options = new ArrayList<>();
        Object val = PhoenixHelper.getIndexSaltBuckets(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("SALT_BUCKETS=%d", val));
        }
        val = PhoenixHelper.getIndexDisableWal(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("DISABLE_WAL=%b", val));
        }
        val = PhoenixHelper.getIndexImmutableRows(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("IMMUTABLE_ROWS=%b", val));
        }
        val = PhoenixHelper.getIndexMultiTelnet(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("MULTI_TELNET=%b", val));
        }
        val = PhoenixHelper.getIndexDefaultColumnFamily(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("DEFAULT_COLUMN_FAMILY='%s'", val));
        }
        val = PhoenixHelper.getIndexStoreNulls(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("STORE_NULLS=%b", val));
        }
        val = PhoenixHelper.getIndexTransactional(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("TRANSACTIONAL=%b", val));
        }
        val = PhoenixHelper.getIndexUpdateCacheFrequency(index);
        if (Objects.nonNull(val)) {
            UpdateCacheFrequencyInfo updateCacheFrequencyInfo = (UpdateCacheFrequencyInfo) val;
            if (updateCacheFrequencyInfo.isMatchLong()) {
                options.add(String.format("UPDATE_CACHE_FREQUENCY=%d", updateCacheFrequencyInfo.getLongValue()));
            } else if (updateCacheFrequencyInfo.isMatchUpdateCacheFrequency()) {
                options.add(String.format("UPDATE_CACHE_FREQUENCY='%s'",
                        updateCacheFrequencyInfo.getUpdateCacheFrequencyValue().sqlFragment()));
            }
        }
        val = PhoenixHelper.getIndexAppendOnlySchema(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("APPEND_ONLY_SCHEMA=%b", val));
        }
        val = PhoenixHelper.getIndexAutoPartitionSeq(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("AUTO_PARTITION_SEQ='%s'", val));
        }
        val = PhoenixHelper.getIndexGuidPostsWidth(index);
        if (Objects.nonNull(val)) {
            options.add(String.format("GUIDE_POSTS_WIDTH=%d", val));
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == 0) {
                sb.append(' ');
            } else {
                sb.append(", ");
            }
            sb.append(options.get(i));
        }
        // 追加 SPLIT ON。
        List<String> splitOnColumns = PhoenixHelper.getIndexSplitPoint(index);
        if (!splitOnColumns.isEmpty()) {
            sb.append(" SPLIT ON (");
            for (int i = 0; i < splitOnColumns.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(splitOnColumns.get(i));
            }
            sb.append(')');
        }
        return sb.toString();
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@Nonnull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }
}
