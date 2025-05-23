package com.dwarfeng.subgrade.sdk.database.definition;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Phoenix Helper。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class PhoenixHelper {

    private static final String CUSTOM_NULLABLE = "NULLABLE";
    private static final String CUSTOM_DEFAULT = "DEFAULT";
    private static final String OPTIONAL_NAME_PRIMARY_KEY = "PK";
    private static final String OPTIONAL_TYPE_PRIMARY_KEY = "PRIMARY_KEY";
    private static final String OPTIONAL_TYPE_INDEX = "INDEX";
    private static final String CUSTOM_ASC_COLUMNS = "ASC_COLUMNS";
    private static final String CUSTOM_DESC_COLUMNS = "DESC_COLUMNS";
    private static final String CUSTOM_INDEX_TYPE = "INDEX_TYPE";
    private static final String CUSTOM_CONTEXT_COLUMNS = "CONTEXT_COLUMNS";
    private static final String CUSTOM_ROW_TIMESTAMP_COLUMN = "ROW_TIMESTAMP_COLUMN";
    private static final String CUSTOM_SALT_BUCKETS = "SALT_BUCKETS";
    private static final String CUSTOM_DISABLE_WAL = "DISABLE_WAL";
    private static final String CUSTOM_IMMUTABLE_ROWS = "IMMUTABLE_ROWS";
    private static final String CUSTOM_MULTI_TELNET = "MULTI_TENANT";
    private static final String CUSTOM_DEFAULT_COLUMN_FAMILY = "DEFAULT_COLUMN_FAMILY";
    private static final String CUSTOM_STORE_NULLS = "STORE_NULLS";
    private static final String CUSTOM_TRANSACTIONAL = "TRANSACTIONAL";
    private static final String CUSTOM_UPDATE_CACHE_FREQUENCY = "UPDATE_CACHE_FREQUENCY";
    private static final String CUSTOM_APPEND_ONLY_SCHEMA = "APPEND_ONLY_SCHEMA";
    private static final String CUSTOM_AUTO_PARTITION_SEQ = "AUTO_PARTITION_SEQ";
    private static final String CUSTOM_GUIDE_POSTS_WIDTH = "GUIDE_POSTS_WIDTH";
    private static final String CUSTOM_SPLIT_POINT = "SPLIT_POINT";
    private static final String CUSTOM_INCLUDE_COLUMNS = "INCLUDE_COLUMNS";
    private static final String CUSTOM_ASYNC_TYPE = "ASYNC_TYPE";
    private static final String CACHE_PRIMARY_COLUMNS = "PRIMARY_COLUMNS";
    private static final String CACHE_NON_PRIMARY_COLUMNS = "NON_PRIMARY_COLUMNS";
    private static final String CACHE_FULL_TABLE_NAME = "FULL_TABLE_NAME";

    public enum ColumnNullable {
        NULL("NULL"),
        NOT_NULL("NOT NULL");

        private final String sqlFragment;

        ColumnNullable(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum UpdateCacheFrequency {
        ALWAYS("ALWAYS"),
        NEVER("NEVER");

        private final String sqlFragment;

        UpdateCacheFrequency(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum IndexType {
        GLOBAL("GLOBAL"),
        LOCAL("LOCAL");

        private final String sqlFragment;

        IndexType(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum IndexAsyncType {
        ASYNC("ASYNC"),
        NOT_ASYNC("");

        private final String sqlFragment;

        IndexAsyncType(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public static class UpdateCacheFrequencyInfo {

        private final boolean matchLong;
        private final Long longValue;
        private final boolean matchUpdateCacheFrequency;
        private final UpdateCacheFrequency updateCacheFrequencyValue;

        public UpdateCacheFrequencyInfo(Object object) {
            if (object instanceof Long) {
                this.matchLong = true;
                this.longValue = (Long) object;
                this.matchUpdateCacheFrequency = false;
                this.updateCacheFrequencyValue = null;
            } else if (object instanceof UpdateCacheFrequency) {
                this.matchLong = false;
                this.longValue = null;
                this.matchUpdateCacheFrequency = true;
                this.updateCacheFrequencyValue = (UpdateCacheFrequency) object;
            } else {
                throw new IllegalArgumentException("入口参数 object 的类型非法: " + object.getClass().getCanonicalName());
            }
        }

        public boolean isMatchLong() {
            return matchLong;
        }

        public Long getLongValue() {
            return longValue;
        }

        public boolean isMatchUpdateCacheFrequency() {
            return matchUpdateCacheFrequency;
        }

        public UpdateCacheFrequency getUpdateCacheFrequencyValue() {
            return updateCacheFrequencyValue;
        }

        @Override
        public String toString() {
            return "UpdateCacheFrequencyInfo{" +
                    "matchLong=" + matchLong +
                    ", longValue=" + longValue +
                    ", matchUpdateCacheFrequency=" + matchUpdateCacheFrequency +
                    ", updateCacheFrequencyValue=" + updateCacheFrequencyValue +
                    '}';
        }
    }

    /**
     * 设置数据表的库名称。
     *
     * @param tableDefinition 数据表定义。
     * @param schemaName      数据表的库名称。
     */
    public static void setSchemaName(@Nonnull TableDefinition tableDefinition, String schemaName) {
        tableDefinition.setSchemaName(schemaName);
    }

    public static void setTableName(@Nonnull TableDefinition tableDefinition, String tableName) {
        tableDefinition.setTableName(tableName);
    }

    public static void addColumn(@Nonnull TableDefinition tableDefinition, @Nonnull String columnName, @Nonnull String type) {
        addColumn(tableDefinition, columnName, type, null, null);
    }

    public static void addColumn(
            @Nonnull TableDefinition tableDefinition, @Nonnull String columnName, @Nonnull String type,
            ColumnNullable columnNullable, String defaultValue) {
        makeSureAllColumnNotExists(tableDefinition, Collections.singletonList(columnName));
        ColumnDefinition columnDefinition = new ColumnDefinition();
        columnDefinition.setName(columnName);
        columnDefinition.setType(type);
        columnDefinition.putPropertyIfNotNull(CUSTOM_NULLABLE, columnNullable);
        columnDefinition.putPropertyIfNotNull(CUSTOM_DEFAULT, defaultValue);
        tableDefinition.addColumnDefinition(columnDefinition);
    }

    /**
     * 获取指定的列定义的 Nullable 属性。
     *
     * <p>
     * 当该属性未定义时返回 null。
     *
     * @param columnDefinition 指定的列定义。
     * @return 指定列定义的 Nullable 属性。
     */
    public static ColumnNullable getColumnNullable(@Nonnull ColumnDefinition columnDefinition) {
        return (ColumnNullable) columnDefinition.getProperty(CUSTOM_NULLABLE, null);
    }

    /**
     * 获取指定的列定义的 默认值。
     *
     * <p>
     * 当该属性未定义时返回 null。
     *
     * @param columnDefinition 指定的列定义。
     * @return 指定列定义的 默认值。
     */
    public static String getColumnDefaultValue(@Nonnull ColumnDefinition columnDefinition) {
        return (String) columnDefinition.getProperty(CUSTOM_DEFAULT, null);
    }

    public static void setPrimaryKey(@Nonnull TableDefinition tableDefinition, @Nonnull String... columnNames) {
        setPrimaryKey(tableDefinition, Arrays.asList(columnNames), null, null, null);
    }

    public static void setPrimaryKey(
            @Nonnull TableDefinition tableDefinition, @Nonnull List<String> columnNames, List<String> ascColumnNames,
            List<String> descColumnNames, String rowTimestampColumnName) {
        makeSureAllColumnExists(tableDefinition, columnNames);
        if (Objects.nonNull(ascColumnNames)) {
            makeSureAllColumnExists(tableDefinition, ascColumnNames);
        }
        if (Objects.nonNull(descColumnNames)) {
            makeSureAllColumnExists(tableDefinition, descColumnNames);
        }
        if (Objects.nonNull(rowTimestampColumnName)) {
            makeSureAllColumnExists(tableDefinition, Collections.singletonList(rowTimestampColumnName));
        }

        OptionalDefinition optionalDefinition = new OptionalDefinition();
        optionalDefinition.setName(OPTIONAL_NAME_PRIMARY_KEY);
        optionalDefinition.setType(OPTIONAL_TYPE_PRIMARY_KEY);
        optionalDefinition.putProperty(CUSTOM_CONTEXT_COLUMNS, new ArrayList<>(columnNames));
        if (Objects.nonNull(ascColumnNames)) {
            optionalDefinition.putProperty(CUSTOM_ASC_COLUMNS, new HashSet<>(ascColumnNames));
        }
        if (Objects.nonNull(descColumnNames)) {
            optionalDefinition.putProperty(CUSTOM_DESC_COLUMNS, new HashSet<>(descColumnNames));
        }
        optionalDefinition.putPropertyIfNotNull(CUSTOM_ROW_TIMESTAMP_COLUMN, rowTimestampColumnName);
        tableDefinition.addOptionalDefinition(optionalDefinition);
    }

    /**
     * 获取指定数据表定义中主键的升序列名称。
     *
     * <p>
     * 该方法不会返回 null，当数据表定义中没有升序列或者未指定升序列时，返回空集合。
     *
     * @param tableDefinition 指定的数据表定义。
     * @return 升序列名称组成的集合。
     */
    public static Set<String> getPrimaryKeyAscColumnNames(@Nonnull TableDefinition tableDefinition) {
        OptionalDefinition primaryKeyOptionalDefinition = getPrimaryKeyOptionalDefinition(tableDefinition);
        @SuppressWarnings("unchecked")
        Set<String> property = (Set<String>) primaryKeyOptionalDefinition.getProperty(
                CUSTOM_ASC_COLUMNS, Collections.emptySet());
        return property;
    }

    /**
     * 获取指定数据表定义中主键的降序列名称。
     *
     * <p>
     * 该方法不会返回 null，当数据表定义中没有降序列或者未指定降序列时，返回空集合。
     *
     * @param tableDefinition 指定的数据表定义。
     * @return 降序列名称组成的集合。
     */
    public static Set<String> getPrimaryKeyDescColumnNames(@Nonnull TableDefinition tableDefinition) {
        OptionalDefinition primaryKeyOptionalDefinition = getPrimaryKeyOptionalDefinition(tableDefinition);
        @SuppressWarnings("unchecked")
        Set<String> property = (Set<String>) primaryKeyOptionalDefinition.getProperty(
                CUSTOM_DESC_COLUMNS, Collections.emptySet());
        return property;
    }

    /**
     * 获取指定数据表定义的 RowTimestamp 所在列名称。
     *
     * <p>
     * 当该属性未被指定时，返回 null。
     *
     * @param tableDefinition 指定的数据表定义。
     * @return 指定数据表定义的 RowTimestamp 所在列名称。
     */
    public static String getPrimaryKeyRowTimestampColumn(@Nonnull TableDefinition tableDefinition) {
        OptionalDefinition primaryKeyOptionalDefinition = getPrimaryKeyOptionalDefinition(tableDefinition);
        return (String) primaryKeyOptionalDefinition.getProperty(CUSTOM_ROW_TIMESTAMP_COLUMN, null);
    }

    private static OptionalDefinition getPrimaryKeyOptionalDefinition(@Nonnull TableDefinition tableDefinition) {
        return tableDefinition.getOptionalDefinitions(OPTIONAL_TYPE_PRIMARY_KEY)
                .stream().findAny().orElseThrow(() -> new IllegalArgumentException("参数 tableDefinition 不含有主键"));
    }

    /**
     * numeric property causes an extra byte to be transparently prepended to every row key to ensure an evenly
     * distributed read and write load across all region servers. This is especially useful when your row key is always
     * monotonically increasing and causing hot spotting on a single region server. However, even if it's not, it often
     * improves performance by ensuring an even distribution of data across your cluster.  The byte is determined by
     * hashing the row key and modding it with the SALT_BUCKETS value. The value may be from 0 to 256, with 0 being a
     * special means of turning salting off for an index in which the data table is salted (since by default an index
     * has the same number of salt buckets as its data table). If split points are not defined for the table, the table
     * will automatically be pre-split at each possible salt bucket value. For more information,
     * see <a href=http://phoenix.incubator.apache.org/salted.html>http://phoenix.incubator.apache.org/salted.html<a/>
     */
    @SuppressWarnings("JavadocLinkAsPlainText")
    public static void setTableSaltBuckets(@Nonnull TableDefinition tableDefinition, Integer val) {
        if (Objects.nonNull(val) && (val < 0 || val > 256)) {
            throw new IllegalArgumentException("SALT_BUCKETS 的取值在 0-256 之前，当前值是 " + val);
        }
        tableDefinition.putProperty(CUSTOM_SALT_BUCKETS, val);
    }

    public static Integer getTableSaltBuckets(@Nonnull TableDefinition tableDefinition) {
        return (Integer) tableDefinition.getProperty(CUSTOM_SALT_BUCKETS, null);
    }

    /**
     * boolean option when true causes HBase not to write data to the write-ahead-log, thus making updates faster at
     * the expense of potentially losing data in the event of a region server failure. This option is useful when
     * updating a table which is not the source-of-truth and thus making the lose of data acceptable.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setTableDisableWal(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_DISABLE_WAL, val);
    }

    public static Boolean getTableDisableWal(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_DISABLE_WAL, null);
    }

    /**
     * boolean option when true declares that your table has rows which are write-once, append-only (i.e. the same row
     * is never updated). With this option set, indexes added to the table are managed completely on the client-side,
     * with no need to perform incremental index maintenance, thus improving performance. Deletes of rows in immutable
     * tables are allowed with some restrictions if there are indexes on the table. Namely, the WHERE clause may not
     * filter on columns not contained by every index. Upserts are expected to never update an existing row (failure to
     * follow this will result in invalid indexes). For more information, see
     * <a href = http://phoenix.incubator.apache.org/secondary_indexing.html>
     * http://phoenix.incubator.apache.org/secondary_indexing.html</a>
     */
    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    public static void setTableImmutableRows(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_IMMUTABLE_ROWS, val);
    }

    public static Boolean getTableImmutableRows(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_IMMUTABLE_ROWS, null);
    }

    /**
     * boolean option when true enables views to be created over the table across different tenants. This option is
     * useful to share the same physical HBase table across many different tenants. For more information, see
     * <a href = http://phoenix.incubator.apache.org/multi-tenancy.html>
     * http://phoenix.incubator.apache.org/multi-tenancy.html</a>
     */
    public static void setTableMultiTelnet(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_MULTI_TELNET, val);
    }

    public static Boolean getTableMultiTelnet(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_MULTI_TELNET, null);
    }

    /**
     * string option determines the column family used used when none is specified. The value is case sensitive.
     * If this option is not present, a column family name of '0' is used.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setTableDefaultColumnFamily(@Nonnull TableDefinition tableDefinition, String val) {
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("DEFAULT_COLUMN_FAMILY 不能为 null 或空字符串");
        }
        tableDefinition.putProperty(CUSTOM_DEFAULT_COLUMN_FAMILY, val);
    }

    public static String getTableDefaultColumnFamily(@Nonnull TableDefinition tableDefinition) {
        return (String) tableDefinition.getProperty(CUSTOM_DEFAULT_COLUMN_FAMILY, null);
    }

    /**
     * boolean option (available as of Phoenix 4.3) determines whether or not null values should be explicitly stored
     * in HBase. This option is generally only useful if a table is configured to store multiple versions in order to
     * facilitate doing flashback queries (i.e. queries to look at the state of a record in the past).
     */
    public static void setTableStoreNulls(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_STORE_NULLS, val);
    }

    public static Boolean getTableStoreNulls(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_STORE_NULLS, null);
    }

    /**
     * option (available as of Phoenix 4.7) determines whether a table (and its secondary indexes) are tranactional.
     * The default value is FALSE, but may be overriden with the phoenix.table.istransactional.default property.
     * A table may be altered to become transactional, but it cannot be transitioned back to be non transactional.
     * For more information on transactions, see
     * <a href = http://phoenix.apache.org/transactions.html>http://phoenix.apache.org/transactions.html</a>
     */
    @SuppressWarnings({"GrazieInspection", "SpellCheckingInspection"})
    public static void setTableTransactional(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_TRANSACTIONAL, val);
    }

    public static Boolean getTableTransactional(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_TRANSACTIONAL, null);
    }

    /**
     * option (available as of Phoenix 4.7) determines how often the server will be checked for meta data updates
     * (for example, the addition or removal of a table column or the updates of table statistics). Possible values are
     * ALWAYS (the default), NEVER, and a millisecond numeric value. An ALWAYS value will cause the client to check
     * with the server each time a statement is executed that references a table (or once per commit for an UPSERT
     * VALUES statement).  A millisecond value indicates how long the client will hold on to its cached version of the
     * metadata before checking back with the server for updates.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setTableUpdateCacheFrequency(@Nonnull TableDefinition tableDefinition, UpdateCacheFrequency val) {
        tableDefinition.putProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * @see #setTableUpdateCacheFrequency(TableDefinition, UpdateCacheFrequency)
     */
    public static void setTableUpdateCacheFrequency(@Nonnull TableDefinition tableDefinition, Long val) {
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("UPDATE_CACHE_FREQUENCY 的取值应大于0 " + val);
        }
        tableDefinition.putProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    public static Object getTableUpdateCacheFrequency(@Nonnull TableDefinition tableDefinition) {
        Object property = tableDefinition.getProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, null);
        if (Objects.isNull(property)) {
            return null;
        }
        return new UpdateCacheFrequencyInfo(property);
    }

    /**
     * boolean option (available as of Phoenix 4.8) when true declares that columns will only be added but never
     * removed from a table. With this option set we can prevent the RPC from the client to the server to fetch the
     * table metadata when the client already has all columns declared in a
     */
    public static void setTableAppendOnlySchema(@Nonnull TableDefinition tableDefinition, Boolean val) {
        tableDefinition.putProperty(CUSTOM_APPEND_ONLY_SCHEMA, val);
    }

    public static Boolean getTableAppendOnlySchema(@Nonnull TableDefinition tableDefinition) {
        return (Boolean) tableDefinition.getProperty(CUSTOM_APPEND_ONLY_SCHEMA, null);
    }

    /**
     * string option (available as of Phoenix 4.8) when set on a base table determines the sequence used to
     * automatically generate a WHERE clause with the first PK column and the unique identifier from the sequence for
     * child views. With this option set, we prevent allocating a sequence in the event that the view already exists.
     */
    public static void setTableAutoPartitionSeq(@Nonnull TableDefinition tableDefinition, String val) {
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("AUTO_PARTITION_SEQ 不能为 null 或空字符串");
        }
        tableDefinition.putProperty(CUSTOM_AUTO_PARTITION_SEQ, val);
    }

    public static String getTableAutoPartitionSeq(@Nonnull TableDefinition tableDefinition) {
        return (String) tableDefinition.getProperty(CUSTOM_AUTO_PARTITION_SEQ, null);
    }

    /**
     * option (available as of Phoenix 4.9) enables specifying a different guidepost width per table. The guidepost
     * width determines the byte sized chunk of work over which a query will be parallelized. A value of 0 means that
     * no guideposts should be collected for the table. A value of null removes any table specific guidepost setting,
     * causing the global server-side phoenix.stats.guidepost.width config parameter to be used again. For more
     * information, see the Statistics Collection page.
     */
    public static void setTableGuidPostsWidth(@Nonnull TableDefinition tableDefinition, Integer val) {
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("GUIDE_POSTS_WIDTH 的取值应大于0 " + val);
        }
        tableDefinition.putProperty(CUSTOM_GUIDE_POSTS_WIDTH, val);
    }

    public static Integer getTableGuidPostsWidth(@Nonnull TableDefinition tableDefinition) {
        return (Integer) tableDefinition.getProperty(CUSTOM_GUIDE_POSTS_WIDTH, null);
    }

    /**
     * 设置数据表的分割点。
     *
     * @param columnNames 分割点的名称。
     */
    public static void setTableSplitPoint(@Nonnull TableDefinition tableDefinition, String... columnNames) {
        makeSureAllColumnExists(tableDefinition, Arrays.asList(columnNames));
        tableDefinition.putProperty(CUSTOM_SPLIT_POINT, Arrays.asList(columnNames));
    }

    public static List<String> getTableSplitPoint(@Nonnull TableDefinition tableDefinition) {
        @SuppressWarnings("unchecked")
        List<String> property = (List<String>) tableDefinition.getProperty(CUSTOM_SPLIT_POINT, Collections.emptyList());
        return property;
    }

    public static void addIndex(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, @Nonnull List<String> columnNames) {
        addIndex(tableDefinition, indexName, columnNames, null, null, null, null, null);
    }

    public static void addIndex(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, @Nonnull List<String> columnNames,
            IndexType indexType, List<String> ascColumnNames, List<String> descColumnNames,
            List<String> includeColumnNames, IndexAsyncType indexAsyncType) {
        makeSureAllColumnExists(tableDefinition, columnNames);
        if (Objects.nonNull(ascColumnNames)) {
            makeSureAllColumnExists(tableDefinition, ascColumnNames);
        }
        if (Objects.nonNull(descColumnNames)) {
            makeSureAllColumnExists(tableDefinition, descColumnNames);
        }
        if (Objects.nonNull(includeColumnNames)) {
            makeSureAllColumnExists(tableDefinition, includeColumnNames);
        }

        OptionalDefinition optionalDefinition = new OptionalDefinition();
        optionalDefinition.setName(indexName);
        optionalDefinition.setType(OPTIONAL_TYPE_INDEX);
        optionalDefinition.putProperty(CUSTOM_CONTEXT_COLUMNS, new ArrayList<>(columnNames));
        optionalDefinition.putPropertyIfNotNull(CUSTOM_INDEX_TYPE, indexType);
        if (Objects.nonNull(ascColumnNames)) {
            optionalDefinition.putProperty(CUSTOM_ASC_COLUMNS, new HashSet<>(ascColumnNames));
        }
        if (Objects.nonNull(descColumnNames)) {
            optionalDefinition.putProperty(CUSTOM_DESC_COLUMNS, new HashSet<>(descColumnNames));
        }
        if (Objects.nonNull(includeColumnNames)) {
            optionalDefinition.putProperty(CUSTOM_INCLUDE_COLUMNS, new ArrayList<>(includeColumnNames));
        }
        optionalDefinition.putPropertyIfNotNull(CUSTOM_ASYNC_TYPE, indexAsyncType);
        tableDefinition.addOptionalDefinition(optionalDefinition);
    }

    public static List<OptionalDefinition> getIndexes(@Nonnull TableDefinition tableDefinition) {
        return tableDefinition.getOptionalDefinitions(OPTIONAL_TYPE_INDEX);
    }

    public static IndexType getIndexType(@Nonnull OptionalDefinition optionalDefinition) {
        return (IndexType) optionalDefinition.getProperty(CUSTOM_INDEX_TYPE, null);
    }

    public static List<String> getIndexContextColumnNames(@Nonnull OptionalDefinition optionalDefinition) {
        @SuppressWarnings("unchecked")
        List<String> property =
                (List<String>) optionalDefinition.getProperty(CUSTOM_CONTEXT_COLUMNS, Collections.emptyList());
        return property;
    }

    /**
     * 获取指定索引的升序列名称。
     *
     * <p>
     * 该方法不会返回 null，当数据表定义中没有升序列或者未指定升序列时，返回空集合。
     *
     * @param optionalDefinition 指定的数据表定义。
     * @return 升序列名称组成的集合。
     */
    public static Set<String> getIndexAscColumnNames(@Nonnull OptionalDefinition optionalDefinition) {
        @SuppressWarnings("unchecked")
        Set<String> property = (Set<String>) optionalDefinition.getProperty(CUSTOM_ASC_COLUMNS, Collections.emptySet());
        return property;
    }

    /**
     * 获取指定索引的降序列名称。
     *
     * <p>
     * 该方法不会返回 null，当数据表定义中没有降序列或者未指定降序列时，返回空集合。
     *
     * @param optionalDefinition 指定的数据表定义。
     * @return 降序列名称组成的集合。
     */
    public static Set<String> getIndexDescColumnNames(@Nonnull OptionalDefinition optionalDefinition) {
        @SuppressWarnings("unchecked")
        Set<String> property = (Set<String>) optionalDefinition.getProperty(CUSTOM_DESC_COLUMNS, Collections.emptySet());
        return property;
    }

    public static List<String> getIndexIncludeColumnNames(@Nonnull OptionalDefinition optionalDefinition) {
        @SuppressWarnings("unchecked")
        List<String> property =
                (List<String>) optionalDefinition.getProperty(CUSTOM_INCLUDE_COLUMNS, Collections.emptyList());
        return property;
    }

    public static IndexAsyncType getIndexAsyncType(@Nonnull OptionalDefinition optionalDefinition) {
        return (IndexAsyncType) optionalDefinition.getProperty(CUSTOM_ASYNC_TYPE, null);
    }

    /**
     * numeric property causes an extra byte to be transparently prepended to every row key to ensure an evenly
     * distributed read and write load across all region servers. This is especially useful when your row key is always
     * monotonically increasing and causing hot spotting on a single region server. However, even if it's not, it often
     * improves performance by ensuring an even distribution of data across your cluster.  The byte is determined by
     * hashing the row key and modding it with the SALT_BUCKETS value. The value may be from 0 to 256, with 0 being a
     * special means of turning salting off for an index in which the data table is salted (since by default an index
     * has the same number of salt buckets as its data table). If split points are not defined for the table, the table
     * will automatically be pre-split at each possible salt bucket value. For more information,
     * see <a href=http://phoenix.incubator.apache.org/salted.html>http://phoenix.incubator.apache.org/salted.html<a/>
     */
    @SuppressWarnings("JavadocLinkAsPlainText")
    public static void setIndexSaltBuckets(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Integer val) {
        makeSureIndexExists(tableDefinition, indexName);
        if (Objects.nonNull(val) && (val < 0 || val > 256)) {
            throw new IllegalArgumentException("SALT_BUCKETS 的取值在 0-256 之前，当前值是 " + val);
        }
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_SALT_BUCKETS, val);
    }

    public static Integer getIndexSaltBuckets(@Nonnull OptionalDefinition optionalDefinition) {
        return (Integer) optionalDefinition.getProperty(CUSTOM_SALT_BUCKETS, null);
    }

    /**
     * boolean option when true causes HBase not to write data to the write-ahead-log, thus making updates faster at
     * the expense of potentially losing data in the event of a region server failure. This option is useful when
     * updating a table which is not the source-of-truth and thus making the lose of data acceptable.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setIndexDisableVal(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_DISABLE_WAL, val);
    }

    public static Boolean getIndexDisableWal(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_DISABLE_WAL, null);
    }

    /**
     * boolean option when true declares that your table has rows which are write-once, append-only (i.e. the same row
     * is never updated). With this option set, indexes added to the table are managed completely on the client-side,
     * with no need to perform incremental index maintenance, thus improving performance. Deletes of rows in immutable
     * tables are allowed with some restrictions if there are indexes on the table. Namely, the WHERE clause may not
     * filter on columns not contained by every index. Upserts are expected to never update an existing row (failure to
     * follow this will result in invalid indexes). For more information, see
     * <a href = http://phoenix.incubator.apache.org/secondary_indexing.html>
     * http://phoenix.incubator.apache.org/secondary_indexing.html</a>
     */
    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    public static void setIndexImmutableRows(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_IMMUTABLE_ROWS, val);
    }

    public static Boolean getIndexImmutableRows(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_IMMUTABLE_ROWS, null);
    }

    /**
     * boolean option when true enables views to be created over the table across different tenants. This option is
     * useful to share the same physical HBase table across many different tenants. For more information, see
     * <a href = http://phoenix.incubator.apache.org/multi-tenancy.html>
     * http://phoenix.incubator.apache.org/multi-tenancy.html</a>
     */
    public static void setIndexMultiTelnet(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_MULTI_TELNET, val);
    }

    public static Boolean getIndexMultiTelnet(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_MULTI_TELNET, null);
    }

    /**
     * string option determines the column family used used when none is specified. The value is case sensitive.
     * If this option is not present, a column family name of '0' is used.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setIndexDefaultColumnFamily(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, String val) {
        makeSureIndexExists(tableDefinition, indexName);
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("DEFAULT_COLUMN_FAMILY 不能为 null 或空字符串");
        }
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_DEFAULT_COLUMN_FAMILY, val);
    }

    public static String getIndexDefaultColumnFamily(@Nonnull OptionalDefinition optionalDefinition) {
        return (String) optionalDefinition.getProperty(CUSTOM_DEFAULT_COLUMN_FAMILY, null);
    }

    /**
     * boolean option (available as of Phoenix 4.3) determines whether or not null values should be explicitly stored
     * in HBase. This option is generally only useful if a table is configured to store multiple versions in order to
     * facilitate doing flashback queries (i.e. queries to look at the state of a record in the past).
     */
    public static void setIndexStoreNulls(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_STORE_NULLS, val);
    }

    public static Boolean getIndexStoreNulls(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_STORE_NULLS, null);
    }

    /**
     * option (available as of Phoenix 4.7) determines whether a table (and its secondary indexes) are tranactional.
     * The default value is FALSE, but may be overriden with the phoenix.table.istransactional.default property.
     * A table may be altered to become transactional, but it cannot be transitioned back to be non transactional.
     * For more information on transactions, see
     * <a href = http://phoenix.apache.org/transactions.html>http://phoenix.apache.org/transactions.html</a>
     */
    @SuppressWarnings({"GrazieInspection", "SpellCheckingInspection"})
    public static void setIndexTransactional(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_TRANSACTIONAL, val);
    }

    public static Boolean getIndexTransactional(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_TRANSACTIONAL, null);
    }

    /**
     * option (available as of Phoenix 4.7) determines how often the server will be checked for meta data updates
     * (for example, the addition or removal of a table column or the updates of table statistics). Possible values are
     * ALWAYS (the default), NEVER, and a millisecond numeric value. An ALWAYS value will cause the client to check
     * with the server each time a statement is executed that references a table (or once per commit for an UPSERT
     * VALUES statement).  A millisecond value indicates how long the client will hold on to its cached version of the
     * metadata before checking back with the server for updates.
     */
    @SuppressWarnings("GrazieInspection")
    public static void setIndexUpdateCacheFrequency(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, UpdateCacheFrequency val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * @see #setIndexUpdateCacheFrequency(TableDefinition, String, UpdateCacheFrequency)
     */
    public static void setIndexUpdateCacheFrequency(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Long val) {
        makeSureIndexExists(tableDefinition, indexName);
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("UPDATE_CACHE_FREQUENCY 的取值应大于0 " + val);
        }
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    public static Object getIndexUpdateCacheFrequency(@Nonnull OptionalDefinition optionalDefinition) {
        Object property = optionalDefinition.getProperty(CUSTOM_UPDATE_CACHE_FREQUENCY, null);
        if (Objects.isNull(property)) {
            return null;
        }
        return new UpdateCacheFrequencyInfo(property);
    }

    /**
     * boolean option (available as of Phoenix 4.8) when true declares that columns will only be added but never
     * removed from a table. With this option set we can prevent the RPC from the client to the server to fetch the
     * table metadata when the client already has all columns declared in a
     */
    public static void setIndexAppendOnlySchema(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Boolean val) {
        makeSureIndexExists(tableDefinition, indexName);
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_APPEND_ONLY_SCHEMA, val);
    }

    public static Boolean getIndexAppendOnlySchema(@Nonnull OptionalDefinition optionalDefinition) {
        return (Boolean) optionalDefinition.getProperty(CUSTOM_APPEND_ONLY_SCHEMA, null);
    }

    /**
     * string option (available as of Phoenix 4.8) when set on a base table determines the sequence used to
     * automatically generate a WHERE clause with the first PK column and the unique identifier from the sequence for
     * child views. With this option set, we prevent allocating a sequence in the event that the view already exists.
     */
    public static void setIndexAutoPartitionSeq(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, String val) {
        makeSureIndexExists(tableDefinition, indexName);
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("AUTO_PARTITION_SEQ 不能为 null 或空字符串");
        }
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_AUTO_PARTITION_SEQ, val);
    }

    public static String getIndexAutoPartitionSeq(@Nonnull OptionalDefinition optionalDefinition) {
        return (String) optionalDefinition.getProperty(CUSTOM_AUTO_PARTITION_SEQ, null);
    }

    /**
     * option (available as of Phoenix 4.9) enables specifying a different guidepost width per table. The guidepost
     * width determines the byte sized chunk of work over which a query will be parallelized. A value of 0 means that
     * no guideposts should be collected for the table. A value of null removes any table specific guidepost setting,
     * causing the global server-side phoenix.stats.guidepost.width config parameter to be used again. For more
     * information, see the Statistics Collection page.
     */
    public static void setIndexGuidPostsWidth(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, Integer val) {
        makeSureIndexExists(tableDefinition, indexName);
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("GUIDE_POSTS_WIDTH 的取值应大于0 " + val);
        }
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_GUIDE_POSTS_WIDTH, val);
    }

    public static Integer getIndexGuidPostsWidth(@Nonnull OptionalDefinition optionalDefinition) {
        return (Integer) optionalDefinition.getProperty(CUSTOM_GUIDE_POSTS_WIDTH, null);
    }

    /**
     * 设置索引的分割点。
     *
     * @param indexName   索引的名称。
     * @param columnNames 分割点的名称。
     */
    public static void setIndexSplitPoint(
            @Nonnull TableDefinition tableDefinition, @Nonnull String indexName, String... columnNames) {
        makeSureIndexExists(tableDefinition, indexName);
        makeSureAllColumnExists(tableDefinition, Arrays.asList(columnNames));
        tableDefinition.getOptionalDefinition(indexName).putProperty(CUSTOM_SPLIT_POINT, Arrays.asList(columnNames));
    }

    public static List<String> getIndexSplitPoint(@Nonnull OptionalDefinition optionalDefinition) {
        @SuppressWarnings("unchecked")
        List<String> property = (List<String>) optionalDefinition.getProperty(CUSTOM_SPLIT_POINT, Collections.emptyList());
        return property;
    }

    @SuppressWarnings("unchecked")
    public static List<ColumnDefinition> getPrimaryKeyColumns(@Nonnull TableDefinition tableDefinition) {
        return (List<ColumnDefinition>) tableDefinition.getOrPutCache(
                CACHE_PRIMARY_COLUMNS, nonCachePrimaryKeyColumns(tableDefinition));
    }

    private static List<ColumnDefinition> nonCachePrimaryKeyColumns(TableDefinition tableDefinition) {
        List<ColumnDefinition> columnDefinitions = tableDefinition.getOptionalDefinitions(OPTIONAL_TYPE_PRIMARY_KEY)
                .stream().map(o -> {
                    @SuppressWarnings("unchecked")
                    List<String> columnNames = (List<String>) o.getProperty(CUSTOM_CONTEXT_COLUMNS, new ArrayList<>());
                    return columnNames.stream().map(tableDefinition::getColumnDefinition).collect(Collectors.toList());
                }).findAny().orElseThrow(() -> new IllegalArgumentException("参数 tableDefinition 不含有主键"));
        return Collections.unmodifiableList(columnDefinitions);
    }

    @SuppressWarnings("unchecked")
    public static List<ColumnDefinition> getNonPrimaryKeyColumns(@Nonnull TableDefinition tableDefinition) {
        return (List<ColumnDefinition>) tableDefinition.getOrPutCache(
                CACHE_NON_PRIMARY_COLUMNS, nonCacheNonPrimaryKeyColumns(tableDefinition));
    }

    private static List<ColumnDefinition> nonCacheNonPrimaryKeyColumns(TableDefinition tableDefinition) {
        List<ColumnDefinition> primaryKeyColumns = getPrimaryKeyColumns(tableDefinition);
        List<ColumnDefinition> result = new ArrayList<>();
        for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
            if (!primaryKeyColumns.contains(columnDefinition)) result.add(columnDefinition);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * 获取指定数据表定义的完全表名称。
     *
     * @param tableDefinition 指定的数据表定义。
     * @return 数据表定义的完全数据表名称。
     */
    public static String getFullTableName(@Nonnull TableDefinition tableDefinition) {
        return (String) tableDefinition.getOrPutCache(CACHE_FULL_TABLE_NAME, nonCacheFullTableName(tableDefinition));
    }

    private static Object nonCacheFullTableName(TableDefinition tableDefinition) {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(tableDefinition.getSchemaName())) {
            sb.append(tableDefinition.getSchemaName()).append('.');
        }
        sb.append(tableDefinition.getTableName());
        return sb.toString();
    }

    private static void makeSureAllColumnNotExists(TableDefinition tableDefinition, Iterable<String> nameList) {
        for (String name : nameList) {
            if (tableDefinition.containsColumnDefinition(name)) {
                throw new IllegalArgumentException("列名称" + name + "已经存在");
            }
        }
    }

    private static void makeSureAllColumnExists(TableDefinition tableDefinition, Iterable<String> nameList) {
        for (String name : nameList) {
            if (!tableDefinition.containsColumnDefinition(name)) {
                throw new IllegalArgumentException("列名称" + name + "不存在");
            }
        }
    }

    private static void makeSureIndexExists(TableDefinition tableDefinition, String indexName) {
        if (!tableDefinition.containsOptionalDefinition(indexName)) {
            throw new IllegalArgumentException("索引称" + indexName + "不存在");
        } else {
            OptionalDefinition optionalDefinition = tableDefinition.getOptionalDefinition(indexName);
            if (!Objects.equals(optionalDefinition.getType(), OPTIONAL_TYPE_INDEX)) {
                throw new IllegalArgumentException("索引称" + indexName + "不存在");
            }
        }
    }
}
