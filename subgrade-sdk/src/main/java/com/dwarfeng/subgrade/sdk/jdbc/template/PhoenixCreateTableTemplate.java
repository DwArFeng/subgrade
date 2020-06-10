package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.td.PhoenixTDConstants;
import com.dwarfeng.subgrade.sdk.jdbc.td.PhoenixTDConstants.ColumnNullable;
import com.dwarfeng.subgrade.sdk.jdbc.td.PhoenixTDConstants.IndexAsyncType;
import com.dwarfeng.subgrade.sdk.jdbc.td.PhoenixTDConstants.UpdateCacheFrequency;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition.ConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition.IndexDefinition;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * Phoenix 建表模板。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixCreateTableTemplate extends GeneralCreateTableTemplate {

    public PhoenixCreateTableTemplate(@NonNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    protected List<String> internalCreateTableSQL() {
        List<String> sqlList = new ArrayList<>();
        String tableName = tableDefinition.getTableName();
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        ConstraintDefinition pkConstraintDefinition = tableDefinition.getConstraintDefinitions().stream()
                .filter(def -> Objects.equals(def.getType(), PhoenixTDConstants.TYPE_PRIMARY_KEY))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("表定义不包含主键"));
        Map<String, Object> customDefinition = tableDefinition.getCustomDefinition();
        sqlList.add(createTableSQL(tableName, columnDefinitions, pkConstraintDefinition, customDefinition));
        for (IndexDefinition indexDefinition : tableDefinition.getIndexDefinitions()) {
            sqlList.add(createIndexSQL(tableName, indexDefinition));
        }
        return sqlList;
    }

    private String createTableSQL(
            String tableName, List<ColumnDefinition> columnDefinitions, ConstraintDefinition pkConstraintDefinition,
            Map<String, Object> customDefinition) {
        StringBuilder sb = new StringBuilder();
        // 追加 CREATE TABLE IF NOT EXISTS + 表名
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName).append(" (");
        // 追加全列序列。
        for (int i = 0; i < columnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            Map<String, Object> columnCustomDefinition = columnDefinition.getCustomDefinition();
            // 追加列的名称和类型。
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(columnDefinition.getName()).append(' ').append(columnDefinition.getType());
            // 追加 NULL 或者 NOT NULL
            if (columnCustomDefinition.containsKey(PhoenixTDConstants.CUSTOM_NULLABLE)) {
                ColumnNullable nullable =
                        (ColumnNullable) columnCustomDefinition.get(PhoenixTDConstants.CUSTOM_NULLABLE);
                if (nullable == ColumnNullable.NOT_NULL) {
                    sb.append(" NOT NULL");
                } else {
                    sb.append(" NULL");
                }
            }
            // 追加 DEFAULT
            if (columnCustomDefinition.containsKey(PhoenixTDConstants.CUSTOM_DEFAULT)) {
                String defaultValue = (String) columnCustomDefinition.get(PhoenixTDConstants.CUSTOM_NULLABLE);
                sb.append(' ').append(defaultValue);
            }
        }
        // 追加主键约束。
        sb.append(", CONSTRAINT pk PRIMARY KEY (");
        @SuppressWarnings("unchecked")
        Set<String> ascColumnNames = (Set<String>) pkConstraintDefinition.getCustomDefinition()
                .getOrDefault(PhoenixTDConstants.CUSTOM_ASC, Collections.emptySet());
        @SuppressWarnings("unchecked")
        Set<String> descColumnNames = (Set<String>) pkConstraintDefinition.getCustomDefinition()
                .getOrDefault(PhoenixTDConstants.CUSTOM_DESC, Collections.emptySet());
        String rowTimestampColumnName = (String) pkConstraintDefinition.getCustomDefinition()
                .getOrDefault(PhoenixTDConstants.CUSTOM_ROW_TIMESTAMP, null);
        // 追加主键序列。
        for (int i = 0; i < pkConstraintDefinition.getColumnDefinitions().size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
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
        appendOptions(sb, customDefinition);
        // 追加 SPLIT ON。
        appendSplitOn(sb, customDefinition);
        return sb.toString();
    }

    private String createIndexSQL(String tableName, IndexDefinition indexDefinition) {
        StringBuilder sb = new StringBuilder();
        // 追加 CREATE。
        sb.append("CREATE");
        // 按需追加 LOCAL
        if (Objects.equals(indexDefinition.getType(), PhoenixTDConstants.INDEX_TYPE_LOCAL)) {
            sb.append(" LOCAL");
        }
        // 追加 INDEX IF NOT EXISTS + index名称 + ON + 表名
        sb.append(" INDEX IF NOT EXISTS ").append(indexDefinition.getName()).append(" ON ")
                .append(tableName).append(" (");
        // 追加索引列。
        Map<String, Object> customDefinition = indexDefinition.getCustomDefinition();
        List<ColumnDefinition> columnDefinitions = indexDefinition.getColumnDefinitions();
        @SuppressWarnings("unchecked")
        Set<String> ascColumnNames = (Set<String>) customDefinition
                .getOrDefault(PhoenixTDConstants.CUSTOM_ASC, Collections.emptySet());
        @SuppressWarnings("unchecked")
        Set<String> descColumnNames = (Set<String>) customDefinition
                .getOrDefault(PhoenixTDConstants.CUSTOM_DESC, Collections.emptySet());
        for (int i = 0; i < columnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            String columnName = columnDefinition.getName();
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
        @SuppressWarnings("unchecked")
        List<String> includeColumnNames = (List<String>) customDefinition
                .getOrDefault(PhoenixTDConstants.CUSTOM_INCLUDE, Collections.emptySet());
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
        IndexAsyncType indexAsyncType = (IndexAsyncType) customDefinition
                .getOrDefault(PhoenixTDConstants.CUSTOM_ASYNC, null);
        if (Objects.nonNull(indexAsyncType) && indexAsyncType == IndexAsyncType.ASYNC) {
            sb.append(" ASYNC");
        }
        // 追加索引的选项。
        appendOptions(sb, customDefinition);
        // 追加 SPLIT ON。
        appendSplitOn(sb, customDefinition);
        return sb.toString();
    }

    @SuppressWarnings("MalformedFormatString")
    private void appendOptions(StringBuilder sb, Map<String, Object> customDefinition) {
        List<String> options = new ArrayList<>();
        Object val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_SALT_BUCKETS, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("SALT_BUCKETS=%d", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_DISABLE_WAL, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("DISABLE_WAL=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_IMMUTABLE_ROWS, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("IMMUTABLE_ROWS=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_MULTI_TENANT, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("MULTI_TENANT=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_DEFAULT_COLUMN_FAMILY, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("DEFAULT_COLUMN_FAMILY='%s'", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_STORE_NULLS, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("STORE_NULLS=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_TRANSACTIONAL, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("TRANSACTIONAL=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_UPDATE_CACHE_FREQUENCY, null);
        if (Objects.nonNull(val)) {
            if (val instanceof UpdateCacheFrequency) {
                if (val == UpdateCacheFrequency.ALWAYS) {
                    options.add("UPDATE_CACHE_FREQUENCY='ALWAYS'");
                } else {
                    options.add("UPDATE_CACHE_FREQUENCY='NEVER'");
                }
            } else {
                options.add(String.format("UPDATE_CACHE_FREQUENCY=%d", val));
            }
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_APPEND_ONLY_SCHEMA, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("APPEND_ONLY_SCHEMA=%b", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_AUTO_PARTITION_SEQ, null);
        if (Objects.nonNull(val)) {
            options.add(String.format("AUTO_PARTITION_SEQ='%s'", val));
        }
        val = customDefinition.getOrDefault(PhoenixTDConstants.CUSTOM_GUIDE_POSTS_WIDTH, null);
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
    }

    private void appendSplitOn(StringBuilder sb, Map<String, Object> customDefinition) {
        @SuppressWarnings("unchecked")
        List<String> splitOnColumns = (List<String>) customDefinition
                .getOrDefault(PhoenixTDConstants.CUSTOM_SPLIT_POINT, Collections.emptyList());
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
    }
}
