package com.dwarfeng.subgrade.sdk.jdbc.definition;

import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultIndexDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants.ColumnNullable;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants.IndexAsyncType;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants.IndexType;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants.UpdateCacheFrequency;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.IndexDefinition;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ColumnDefinition;

/**
 * Phoenix 数据库表定义帮手。
 *
 * <p>
 * 该类能够帮助您快速的构造适用于 Phoenix 的数据库表定义。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixTableDefinitionHelper {

    private String schemaName = null;
    private String tableName = null;
    private final Map<String, Object> customDefinition = new HashMap<>();
    private final Map<String, ColumnDefinition> columnDefinitionMap = new LinkedHashMap<>();
    private ConstraintDefinition primaryKeyConstraintDefinition = null;
    private final Map<String, IndexDefinition> indexDefinitionMap = new LinkedHashMap<>();

    public PhoenixTableDefinitionHelper() {
        customDefinition.put(PhoenixConstants.CUSTOM_SPLIT_POINT, new ArrayList<>());
    }

    /**
     * 设置数据表的库名称。
     *
     * @param schemaName 数据表的库名称。
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * 设置数据表的名称。
     *
     * @param tableName 数据表的名称。
     */
    public void setTableName(@NonNull String tableName) {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("参数 tableName 不能为空");
        }
        this.tableName = tableName;
    }

    /**
     * 增加列定义。
     *
     * @param columnName 列的名称。
     * @param type       列的类型。
     */
    public void addColumn(@NonNull String columnName, @NonNull String type) {
        addColumn(columnName, type, null, null);
    }

    /**
     * 添加列定义。
     *
     * @param columnName     列的名称。
     * @param type           列的类型。
     * @param columnNullable 列是否允许为 NULL。
     * @param defaultValue   列的默认值。
     */
    public void addColumn(
            @NonNull String columnName, @NonNull String type, ColumnNullable columnNullable, String defaultValue) {
        if (columnDefinitionMap.containsKey(columnName)) {
            throw new IllegalArgumentException("列名称 " + columnName + "已经存在");
        }
        Map<String, Object> customDefinition = new HashMap<>();
        if (Objects.nonNull(columnNullable)) {
            switch (columnNullable) {
                case NULL:
                    customDefinition.put(PhoenixConstants.CUSTOM_NULLABLE, ColumnNullable.NULL);
                    break;
                case NOT_NULL:
                    customDefinition.put(PhoenixConstants.CUSTOM_NULLABLE, ColumnNullable.NOT_NULL);
                    break;
            }
        }
        if (Objects.nonNull(defaultValue)) {
            customDefinition.put(PhoenixConstants.CUSTOM_DEFAULT, defaultValue);
        }
        columnDefinitionMap.put(columnName, new DefaultColumnDefinition(columnName, type, customDefinition));
    }

    /**
     * 设置主键约束定义。
     *
     * @param columnNames 所有主键的名称。
     */
    public void setPrimaryKey(@NonNull String... columnNames) {
        List<String> nameList = Arrays.asList(columnNames);
        makeSureAllColumnExists(nameList);
        Map<String, Object> customDefinition = new HashMap<>();
        customDefinition.put(PhoenixConstants.CUSTOM_ASC, new HashSet<String>());
        customDefinition.put(PhoenixConstants.CUSTOM_DESC, new HashSet<String>());
        primaryKeyConstraintDefinition = new DefaultConstraintDefinition(
                PhoenixConstants.NAME_PRIMARY_KEY, PhoenixConstants.TYPE_PRIMARY_KEY,
                nameList.stream().map(columnDefinitionMap::get).collect(Collectors.toList()), customDefinition);
    }

    /**
     * 设置主键升序排序。
     *
     * @param columnNames 需要设置主键升序排序的列的名称。
     */
    @SuppressWarnings("unchecked")
    public void setPrimaryKeyAsc(@NonNull String... columnNames) {
        List<String> nameList = Arrays.asList(columnNames);
        makeSureAllColumnExists(nameList);
        Map<String, Object> customDefinition = primaryKeyConstraintDefinition.getCustomDefinition();
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_ASC)).addAll(nameList);
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_DESC)).removeAll(nameList);
    }

    /**
     * 设置主键降序排序。
     *
     * @param columnNames 需要设置主键降序排序的列的名称。
     */
    @SuppressWarnings("unchecked")
    public void setPrimaryKeyDesc(@NonNull String... columnNames) {
        List<String> nameList = Arrays.asList(columnNames);
        makeSureAllColumnExists(nameList);
        Map<String, Object> customDefinition = primaryKeyConstraintDefinition.getCustomDefinition();
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_ASC)).removeAll(nameList);
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_DESC)).addAll(nameList);
    }

    /**
     * 设置主键的指定列启用 ROW_TIMESTAMP 属性。
     *
     * @param columnName 自用属性的列名称。
     */
    public void rowTimestampPrimaryKey(@NonNull String columnName) {
        if (!columnDefinitionMap.containsKey(columnName)) {
            throw new IllegalArgumentException("列名称" + columnName + "不存在");
        }
        Map<String, Object> customDefinition = primaryKeyConstraintDefinition.getCustomDefinition();
        customDefinition.put(PhoenixConstants.CUSTOM_ROW_TIMESTAMP, columnName);
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
    public void setTableSaltBuckets(Integer val) {
        if (Objects.nonNull(val) && (val < 0 || val > 256)) {
            throw new IllegalArgumentException("SALT_BUCKETS 的取值在 0-256 之前，当前值是 " + val);
        }
        customDefinition.put(PhoenixConstants.CUSTOM_SALT_BUCKETS, val);
    }

    /**
     * boolean option when true causes HBase not to write data to the write-ahead-log, thus making updates faster at
     * the expense of potentially losing data in the event of a region server failure. This option is useful when
     * updating a table which is not the source-of-truth and thus making the lose of data acceptable.
     */
    public void setTableDisableVal(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_DISABLE_WAL, val);
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
    public void setTableImmutableRows(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_IMMUTABLE_ROWS, val);
    }

    /**
     * boolean option when true enables views to be created over the table across different tenants. This option is
     * useful to share the same physical HBase table across many different tenants. For more information, see
     * <a href = http://phoenix.incubator.apache.org/multi-tenancy.html>
     * http://phoenix.incubator.apache.org/multi-tenancy.html</a>
     */
    public void setTableMultiTelnet(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_MULTI_TENANT, val);
    }

    /**
     * string option determines the column family used used when none is specified. The value is case sensitive.
     * If this option is not present, a column family name of '0' is used.
     */
    public void setTableDefaultColumnFamily(String val) {
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("DEFAULT_COLUMN_FAMILY 不能为 null 或空字符串");
        }
        customDefinition.put(PhoenixConstants.CUSTOM_DEFAULT_COLUMN_FAMILY, val);
    }

    /**
     * boolean option (available as of Phoenix 4.3) determines whether or not null values should be explicitly stored
     * in HBase. This option is generally only useful if a table is configured to store multiple versions in order to
     * facilitate doing flashback queries (i.e. queries to look at the state of a record in the past).
     */
    public void setTableStoreNulls(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_STORE_NULLS, val);
    }

    /**
     * option (available as of Phoenix 4.7) determines whether a table (and its secondary indexes) are tranactional.
     * The default value is FALSE, but may be overriden with the phoenix.table.istransactional.default property.
     * A table may be altered to become transactional, but it cannot be transitioned back to be non transactional.
     * For more information on transactions, see
     * <a href = http://phoenix.apache.org/transactions.html>http://phoenix.apache.org/transactions.html</a>
     */
    public void setTableTransactional(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_TRANSACTIONAL, val);
    }

    /**
     * option (available as of Phoenix 4.7) determines how often the server will be checked for meta data updates
     * (for example, the addition or removal of a table column or the updates of table statistics). Possible values are
     * ALWAYS (the default), NEVER, and a millisecond numeric value. An ALWAYS value will cause the client to check
     * with the server each time a statement is executed that references a table (or once per commit for an UPSERT
     * VALUES statement).  A millisecond value indicates how long the client will hold on to its cached version of the
     * metadata before checking back with the server for updates.
     */
    public void setTableUpdateCacheFrequency(UpdateCacheFrequency val) {
        customDefinition.put(PhoenixConstants.CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * @see #setTableUpdateCacheFrequency(UpdateCacheFrequency)
     */
    public void setTableUpdateCacheFrequency(Long val) {
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("UPDATE_CACHE_FREQUENCY 的取值应大于0 " + val);
        }
        customDefinition.put(PhoenixConstants.CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * boolean option (available as of Phoenix 4.8) when true declares that columns will only be added but never
     * removed from a table. With this option set we can prevent the RPC from the client to the server to fetch the
     * table metadata when the client already has all columns declared in a
     */
    public void setTableAppendOnlySchema(Boolean val) {
        customDefinition.put(PhoenixConstants.CUSTOM_APPEND_ONLY_SCHEMA, val);
    }

    /**
     * string option (available as of Phoenix 4.8) when set on a base table determines the sequence used to
     * automatically generate a WHERE clause with the first PK column and the unique identifier from the sequence for
     * child views. With this option set, we prevent allocating a sequence in the event that the view already exists.
     */
    public void setTableAutoPartitionSeq(String val) {
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("AUTO_PARTITION_SEQ 不能为 null 或空字符串");
        }
        customDefinition.put(PhoenixConstants.CUSTOM_AUTO_PARTITION_SEQ, val);
    }

    /**
     * option (available as of Phoenix 4.9) enables specifying a different guidepost width per table. The guidepost
     * width determines the byte sized chunk of work over which a query will be parallelized. A value of 0 means that
     * no guideposts should be collected for the table. A value of null removes any table specific guidepost setting,
     * causing the global server-side phoenix.stats.guidepost.width config parameter to be used again. For more
     * information, see the Statistics Collection page.
     */
    public void setTableGuidPostsWidth(Integer val) {
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("GUIDE_POSTS_WIDTH 的取值应大于0 " + val);
        }
        customDefinition.put(PhoenixConstants.CUSTOM_GUIDE_POSTS_WIDTH, val);
    }

    /**
     * 设置数据表的分割点。
     *
     * @param columnNames 分割点的名称。
     */
    public void setTableSplitPoint(String... columnNames) {
        makeSureAllColumnExists(Arrays.asList(columnNames));
        customDefinition.put(PhoenixConstants.CUSTOM_SPLIT_POINT, Arrays.asList(columnNames));
    }

    /**
     * 添加索引。
     *
     * @param indexName   索引的名称。
     * @param indexType   所以的类型。
     * @param columnNames 索引包含的列。
     */
    public void addIndex(@NonNull String indexName, @NonNull IndexType indexType, @NonNull String... columnNames) {
        if (indexDefinitionMap.containsKey(indexName)) {
            throw new IllegalArgumentException("索引 " + indexName + " 已经存在");
        }
        List<String> columnNameList = Arrays.asList(columnNames);
        String type = PhoenixConstants.INDEX_TYPE_GLOBAL;
        switch (indexType) {
            case GLOBAL:
                type = PhoenixConstants.INDEX_TYPE_GLOBAL;
                break;
            case LOCAL:
                type = PhoenixConstants.INDEX_TYPE_LOCAL;
                break;
        }
        List<ColumnDefinition> columnDefinitions = columnNameList.stream()
                .map(columnDefinitionMap::get).collect(Collectors.toList());
        makeSureAllColumnExists(columnNameList);
        Map<String, Object> customDefinition = new HashMap<>();
        customDefinition.put(PhoenixConstants.CUSTOM_ASC, new HashSet<String>());
        customDefinition.put(PhoenixConstants.CUSTOM_DESC, new HashSet<String>());
        customDefinition.put(PhoenixConstants.CUSTOM_SPLIT_POINT, new ArrayList<>());
        customDefinition.put(PhoenixConstants.CUSTOM_INCLUDE, new ArrayList<>());
        indexDefinitionMap.put(
                indexName, new DefaultIndexDefinition(indexName, type, columnDefinitions, customDefinition));
    }

    /**
     * 设置索引升序排序。
     *
     * @param indexName   索引的名称。
     * @param columnNames 需要设置索引升序排序的列的名称。
     */
    @SuppressWarnings("unchecked")
    public void setIndexAsc(@NonNull String indexName, @NonNull String... columnNames) {
        List<String> columnNameList = Arrays.asList(columnNames);
        makeSureIndexExists(indexName);
        makeSureAllColumnExists(columnNameList);
        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_ASC)).addAll(columnNameList);
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_DESC)).removeAll(columnNameList);
    }

    /**
     * 设置索引降序排序。
     *
     * @param indexName   索引的名称。
     * @param columnNames 需要设置索引降序排序的列的名称。
     */
    @SuppressWarnings("unchecked")
    public void setIndexDesc(@NonNull String indexName, @NonNull String... columnNames) {
        List<String> columnNameList = Arrays.asList(columnNames);
        makeSureIndexExists(indexName);
        makeSureAllColumnExists(columnNameList);
        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_ASC)).removeAll(columnNameList);
        ((HashSet<String>) customDefinition.get(PhoenixConstants.CUSTOM_DESC)).addAll(columnNameList);
    }

    /**
     * 设置索引的包含列。
     *
     * @param indexName   索引的名称。
     * @param columnNames 索引的包含列。
     */
    public void setIndexInclude(@NonNull String indexName, @NonNull String... columnNames) {
        List<String> columnNameList = Arrays.asList(columnNames);
        makeSureIndexExists(indexName);
        makeSureAllColumnExists(columnNameList);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_INCLUDE, columnNameList);
    }

    /**
     * 设置索引的同步类型。
     *
     * @param indexName      索引的名称。
     * @param indexAsyncType 索引的同步类型。
     */
    public void setIndexAsyncType(@NonNull String indexName, IndexAsyncType indexAsyncType) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_ASYNC, indexAsyncType);
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
    public void setIndexSaltBuckets(String indexName, Integer val) {
        makeSureIndexExists(indexName);
        if (Objects.nonNull(val) && (val < 0 || val > 256)) {
            throw new IllegalArgumentException("SALT_BUCKETS 的取值在 0-256 之前，当前值是 " + val);
        }
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_SALT_BUCKETS, val);
    }

    /**
     * boolean option when true causes HBase not to write data to the write-ahead-log, thus making updates faster at
     * the expense of potentially losing data in the event of a region server failure. This option is useful when
     * updating a table which is not the source-of-truth and thus making the lose of data acceptable.
     */
    public void setIndexDisableVal(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_DISABLE_WAL, val);
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
    public void setIndexImmutableRows(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_IMMUTABLE_ROWS, val);
    }

    /**
     * boolean option when true enables views to be created over the table across different tenants. This option is
     * useful to share the same physical HBase table across many different tenants. For more information, see
     * <a href = http://phoenix.incubator.apache.org/multi-tenancy.html>
     * http://phoenix.incubator.apache.org/multi-tenancy.html</a>
     */
    public void setIndexMultiTelnet(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_MULTI_TENANT, val);
    }

    /**
     * string option determines the column family used used when none is specified. The value is case sensitive.
     * If this option is not present, a column family name of '0' is used.
     */
    public void setIndexDefaultColumnFamily(String indexName, String val) {
        makeSureIndexExists(indexName);
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("DEFAULT_COLUMN_FAMILY 不能为 null 或空字符串");
        }
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_DEFAULT_COLUMN_FAMILY, val);
    }

    /**
     * boolean option (available as of Phoenix 4.3) determines whether or not null values should be explicitly stored
     * in HBase. This option is generally only useful if a table is configured to store multiple versions in order to
     * facilitate doing flashback queries (i.e. queries to look at the state of a record in the past).
     */
    public void setIndexStoreNulls(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_STORE_NULLS, val);
    }

    /**
     * option (available as of Phoenix 4.7) determines whether a table (and its secondary indexes) are tranactional.
     * The default value is FALSE, but may be overriden with the phoenix.table.istransactional.default property.
     * A table may be altered to become transactional, but it cannot be transitioned back to be non transactional.
     * For more information on transactions, see
     * <a href = http://phoenix.apache.org/transactions.html>http://phoenix.apache.org/transactions.html</a>
     */
    public void setIndexTransactional(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_TRANSACTIONAL, val);
    }

    /**
     * option (available as of Phoenix 4.7) determines how often the server will be checked for meta data updates
     * (for example, the addition or removal of a table column or the updates of table statistics). Possible values are
     * ALWAYS (the default), NEVER, and a millisecond numeric value. An ALWAYS value will cause the client to check
     * with the server each time a statement is executed that references a table (or once per commit for an UPSERT
     * VALUES statement).  A millisecond value indicates how long the client will hold on to its cached version of the
     * metadata before checking back with the server for updates.
     */
    public void setIndexUpdateCacheFrequency(String name, UpdateCacheFrequency val) {
        makeSureIndexExists(name);
        indexDefinitionMap.get(name).getCustomDefinition().put(PhoenixConstants.CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * @see #setIndexUpdateCacheFrequency(String, Long)
     */
    public void setIndexUpdateCacheFrequency(String indexName, Long val) {
        makeSureIndexExists(indexName);
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("UPDATE_CACHE_FREQUENCY 的取值应大于0 " + val);
        }
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_UPDATE_CACHE_FREQUENCY, val);
    }

    /**
     * boolean option (available as of Phoenix 4.8) when true declares that columns will only be added but never
     * removed from a table. With this option set we can prevent the RPC from the client to the server to fetch the
     * table metadata when the client already has all columns declared in a
     */
    public void setIndexAppendOnlySchema(String indexName, Boolean val) {
        makeSureIndexExists(indexName);
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_APPEND_ONLY_SCHEMA, val);
    }

    /**
     * string option (available as of Phoenix 4.8) when set on a base table determines the sequence used to
     * automatically generate a WHERE clause with the first PK column and the unique identifier from the sequence for
     * child views. With this option set, we prevent allocating a sequence in the event that the view already exists.
     */
    public void setIndexAutoPartitionSeq(String indexName, String val) {
        makeSureIndexExists(indexName);
        if (Objects.isNull(val) || val.isEmpty()) {
            throw new IllegalArgumentException("AUTO_PARTITION_SEQ 不能为 null 或空字符串");
        }
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_AUTO_PARTITION_SEQ, val);
    }

    /**
     * option (available as of Phoenix 4.9) enables specifying a different guidepost width per table. The guidepost
     * width determines the byte sized chunk of work over which a query will be parallelized. A value of 0 means that
     * no guideposts should be collected for the table. A value of null removes any table specific guidepost setting,
     * causing the global server-side phoenix.stats.guidepost.width config parameter to be used again. For more
     * information, see the Statistics Collection page.
     */
    public void setIndexGuidPostsWidth(String indexName, Integer val) {
        makeSureIndexExists(indexName);
        if (Objects.nonNull(val) && val < 0) {
            throw new IllegalArgumentException("GUIDE_POSTS_WIDTH 的取值应大于0 " + val);
        }
        indexDefinitionMap.get(indexName).getCustomDefinition().put(PhoenixConstants.CUSTOM_GUIDE_POSTS_WIDTH, val);
    }

    /**
     * 设置索引的分割点。
     *
     * @param indexName   索引的名称。
     * @param columnNames 分割点的名称。
     */
    public void setIndexSplitPoint(String indexName, String... columnNames) {
        makeSureIndexExists(indexName);
        makeSureAllColumnExists(Arrays.asList(columnNames));
        indexDefinitionMap.get(indexName).getCustomDefinition()
                .put(PhoenixConstants.CUSTOM_SPLIT_POINT, Arrays.asList(columnNames));
    }

    private void makeSureAllColumnExists(List<String> nameList) {
        if (!columnDefinitionMap.keySet().containsAll(nameList)) {
            for (String name : nameList) {
                if (!columnDefinitionMap.containsKey(name)) {
                    throw new IllegalArgumentException("列名称" + name + "不存在");
                }
            }
        }
    }

    private void makeSureIndexExists(String name) {
        if (!indexDefinitionMap.containsKey(name)) {
            throw new IllegalArgumentException("索引称" + name + "不存在");
        }
    }

    /**
     * 构造数据表定义。
     *
     * @return 构造生成的数据表定义。
     */
    @SuppressWarnings("DuplicatedCode")
    public TableDefinition buildTableDefinition() {
        if (Objects.isNull(tableName)) {
            throw new IllegalArgumentException("您还没有指定表名称，请先调用 setTableName(String)");
        }
        if (Objects.nonNull(schemaName) && tableName.contains(".")) {
            throw new IllegalArgumentException("指定数据库名称时，表名称不应该含有字符 '.'");
        }
        if (columnDefinitionMap.isEmpty()) {
            throw new IllegalArgumentException("您还没有指定表中的任何列，请先调用 addColumn(String, String)");
        }
        if (Objects.isNull(primaryKeyConstraintDefinition)) {
            throw new IllegalArgumentException("您还没有指定表的主键，请先调用 setPrimaryKey(String...)");
        }

        return new DefaultTableDefinition(
                schemaName,
                tableName,
                new ArrayList<>(columnDefinitionMap.values()),
                Collections.singletonList(primaryKeyConstraintDefinition),
                new ArrayList<>(indexDefinitionMap.values()),
                customDefinition
        );
    }
}
