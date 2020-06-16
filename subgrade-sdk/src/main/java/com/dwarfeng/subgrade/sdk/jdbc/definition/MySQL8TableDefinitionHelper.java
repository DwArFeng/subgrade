package com.dwarfeng.subgrade.sdk.jdbc.definition;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.DefaultTableDefinition.DefaultIndexDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.MySQL8Constants.*;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.ConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition.IndexDefinition;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MySQL8 数据库表定义帮手。
 *
 * <p>
 * 该类能够帮助您快速的构造适用于 MySQL8 的数据库表定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MySQL8TableDefinitionHelper {

    private String schemaName;
    private String tableName;
    private final Map<String, Object> customDefinition = new HashMap<>();
    private final Map<String, ColumnDefinition> columnDefinitionMap = new LinkedHashMap<>();
    private final Map<String, ConstraintDefinition> constraintDefinitionMap = new LinkedHashMap<>();
    private final Map<String, IndexDefinition> indexDefinitionMap = new LinkedHashMap<>();

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
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 设置数据库引擎。
     *
     * @param engine 数据库引擎。
     */
    public void setEngine(@NonNull Engine engine) {
        customDefinition.put(MySQL8Constants.CUSTOM_ENGINE, engine);
    }

    /**
     * 设置数据库的默认字符集。
     *
     * @param charset 数据库的默认字符集。
     */
    public void setDefaultCharset(@NonNull Charset charset) {
        customDefinition.put(MySQL8Constants.CUSTOM_DEFAULT_CHARSET, charset);
    }

    /**
     * 设置数据库字符的对比方法。
     *
     * @param collation 数据库字符的对比方法。
     */
    public void setCollation(@NonNull Collation collation) {
        customDefinition.put(MySQL8Constants.CUSTOM_COLLATION, collation);
    }

    /**
     * 设置数据库自增列的自增大小。
     *
     * @param autoIncrement 数据库自增列的自增大小。
     */
    public void setAutoIncrement(int autoIncrement) {
        customDefinition.put(MySQL8Constants.CUSTOM_AUTO_INCREMENT, autoIncrement);
    }

    /**
     * 增加列定义。
     *
     * @param columnName 列的名称。
     * @param type       列的类型。
     */
    public void addColumn(@NonNull String columnName, @NonNull String type) {
        addColumn(columnName, type, null, null, null);
    }

    /**
     * 增加列定义。
     *
     * @param columnName       列的名称。
     * @param type             列的类型。
     * @param defaultValue     列的默认值。
     * @param columnProperties 列的属性。
     */
    public void addColumn(
            @NonNull String columnName, @NonNull String type, String defaultValue, Charset characterSet,
            Collation collate, ColumnProperty... columnProperties) {
        makeSureAllColumnNotExists(Collections.singletonList(columnName));
        makeSureColumnPropertiesValid(Arrays.asList(columnProperties));
        Map<String, Object> customDefinition = new HashMap<>();
        customDefinition.put(MySQL8Constants.CUSTOM_DEFAULT, defaultValue);
        customDefinition.put(MySQL8Constants.CUSTOM_COLUMN_PROPERTY, Arrays.asList(columnProperties));
        if (Objects.nonNull(characterSet)) {
            customDefinition.put(MySQL8Constants.CUSTOM_CHARACTER_SET, characterSet);
        }
        if (Objects.nonNull(collate)) {
            customDefinition.put(MySQL8Constants.CUSTOM_COLLATE, collate);
        }
        columnDefinitionMap.put(columnName, new DefaultColumnDefinition(columnName, type, customDefinition));
    }

    private void makeSureColumnPropertiesValid(List<ColumnProperty> columnProperties) {
        if (CollectionUtil.conatinsNull(columnProperties)) {
            throw new IllegalArgumentException("入口参数 columnProperties 不允许含有 NULL 元素");
        }
        boolean nullFlag = false;
        boolean notNullFlag = false;
        for (ColumnProperty columnProperty : columnProperties) {
            if (Objects.equals(columnProperty, ColumnProperty.NULL)) {
                nullFlag = true;
            } else if (Objects.equals(columnProperty, ColumnProperty.NOT_NULL)) {
                notNullFlag = true;
            }
        }
        if (nullFlag && notNullFlag) {
            throw new IllegalArgumentException("入口参数 columnProperties 不允许同时包含 NULL 和 NOT_NULL");
        }
    }

    /**
     * 添加索引。
     *
     * @param name        索引的名称。
     * @param indexType   索引的类型。
     * @param columnNames 与索引相关的列名称。
     */
    public void addIndex(@NonNull String name, @NonNull IndexType indexType, @NonNull String... columnNames) {
        addIndex(name, indexType, null, null, null, columnNames);
    }

    /**
     * 添加索引。
     *
     * @param name         索引的名称。
     * @param indexType    索引的类型。
     * @param indexStorage 索引的存储。
     * @param keyBlockSize 索引的 KEY_BLOCK_SIZE。
     * @param parser       索引的 parser。
     * @param columnNames  与索引有关的列名称。
     */
    public void addIndex(
            @NonNull String name, @NonNull IndexType indexType, IndexStorage indexStorage,
            Integer keyBlockSize, String parser, @NonNull String... columnNames) {
        List<String> nameList = Arrays.asList(columnNames);
        makeSureAllColumnExists(nameList);
        Map<String, Object> customDefinition = new HashMap<>();
        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_TYPE, indexType);
        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_LENGTH, new HashMap<String, Integer>());
        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_ASC, new HashSet<String>());
        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_DESC, new HashSet<String>());
        if (Objects.nonNull(indexStorage)) {
            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_STORAGE, indexStorage);
        }
        if (Objects.nonNull(keyBlockSize)) {
            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_KEY_BLOCK_SIZE, keyBlockSize);
        }
        if (Objects.nonNull(parser)) {
            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_PARSER, parser);
        }
        indexDefinitionMap.put(name, new DefaultIndexDefinition(
                MySQL8Constants.NAME_PRIMARY_KEY, MySQL8Constants.TYPE_INDEX,
                nameList.stream().map(columnDefinitionMap::get).collect(Collectors.toList()), customDefinition));
    }

    /**
     * 设置索引的长度。
     *
     * @param indexName  索引的名称。
     * @param columnName 需要设置索引长度的列。
     * @param length     索引的长度。
     */
    @SuppressWarnings("unchecked")
    public void setIndexLength(@NonNull String indexName, @NonNull String columnName, Integer length) {
        makeSureIndexExists(indexName);
        makeSureAllColumnExists(Collections.singletonList(columnName));
        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
        HashMap<String, Integer> lengthMap =
                (HashMap<String, Integer>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_LENGTH);
        if (Objects.isNull(length)) {
            lengthMap.remove(columnName);
        } else {
            lengthMap.put(columnName, length);
        }
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
        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_ASC)).addAll(columnNameList);
        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_DESC)).removeAll(columnNameList);
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
        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_ASC)).removeAll(columnNameList);
        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_DESC)).addAll(columnNameList);
    }

    /**
     * 设置外键约束。
     *
     * @param foreignKeyName 外键约束的名称。
     * @param refSchemaName  外键约束引用的数据库名称。
     * @param refTableName   外键约束引用的数据表名称。
     * @param refColumnNames 外键约束引用的列名称。
     * @param columnNames    与外键约束有关的列名称。
     */
    public void addForeignKey(
            @NonNull String foreignKeyName, String refSchemaName, @NonNull String refTableName,
            @NonNull List<String> refColumnNames, @NonNull List<String> columnNames) {
        addForeignKey(foreignKeyName, refSchemaName, refTableName, refColumnNames, columnNames, null, null);
    }

    /**
     * 设置外键约束。
     *
     * @param foreignKeyName 外键约束的名称。
     * @param refSchemaName  外键约束引用的数据库名称。
     * @param refTableName   外键约束引用的数据表名称。
     * @param refColumnNames 外键约束引用的列名称。
     * @param columnNames    与外键约束有关的列名称。
     * @param onUpdate       有关列更新时的动作。
     * @param onDelete       有关列删除时的动作。
     */
    public void addForeignKey(
            @NonNull String foreignKeyName, String refSchemaName, @NonNull String refTableName,
            @NonNull List<String> refColumnNames, @NonNull List<String> columnNames, ForeignKeyOption onUpdate,
            ForeignKeyOption onDelete) {
        makeSureAllColumnExists(columnNames);
        Map<String, Object> customDefinition = new HashMap<>();
        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_SCHEMA, refSchemaName);
        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_TABLE, refTableName);
        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_COLUMNS, refColumnNames);
        if (Objects.nonNull(onUpdate)) {
            customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_ON_UPDATE, onUpdate);
        }
        if (Objects.nonNull(onDelete)) {
            customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_ON_DELETE, onDelete);
        }
        constraintDefinitionMap.put(foreignKeyName, new DefaultConstraintDefinition(
                foreignKeyName, MySQL8Constants.TYPE_FOREIGN_KEY,
                columnNames.stream().map(columnDefinitionMap::get).collect(Collectors.toList()), customDefinition));
    }

    private void makeSureAllColumnNotExists(List<String> nameList) {
        for (String name : nameList) {
            if (columnDefinitionMap.containsKey(name)) {
                throw new IllegalArgumentException("列名称 " + name + "已经存在");
            }
        }
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
        long count = indexDefinitionMap.entrySet().stream().filter(
                entry -> Objects.equals(entry.getValue().getCustomDefinition().get(MySQL8Constants.CUSTOM_INDEX_TYPE),
                        IndexType.PRIMARY)).map(Map.Entry::getKey).count();
        if (count == 0) {
            throw new IllegalArgumentException("您还没有指定表中的主键，先调用 addIndex(String, IndexType, " +
                    "IndexStorage, Integer, String, String...) 方法添加类型为 IndexType.PRIMARY的主键");
        }
        if (count > 1) {
            throw new IllegalArgumentException("您指定了多个主键索引，请检查程序并去除多余的主键索引");
        }

        return new DefaultTableDefinition(
                schemaName,
                tableName,
                new ArrayList<>(columnDefinitionMap.values()),
                new ArrayList<>(constraintDefinitionMap.values()),
                new ArrayList<>(indexDefinitionMap.values()),
                customDefinition
        );
    }
}
