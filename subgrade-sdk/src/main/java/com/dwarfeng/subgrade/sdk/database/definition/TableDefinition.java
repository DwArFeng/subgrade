package com.dwarfeng.subgrade.sdk.database.definition;

import com.dwarfeng.dutil.basic.cna.model.*;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 数据表定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class TableDefinition {

    private final ReferenceModel<String> schemaNameRef = new DefaultReferenceModel<>();
    private final ReferenceModel<String> tableNameRef = new DefaultReferenceModel<>();
    private final KeyListModel<String, ColumnDefinition> columnDefinitionList = new DelegateKeyListModel<>();
    private final KeyListModel<String, OptionalDefinition> optionalDefinitionList = new DelegateKeyListModel<>();
    private final MapModel<String, Object> propertyMap = new DelegateMapModel<>();
    private final Map<String, Object> cacheMap = new HashMap<>();

    /**
     * 获取数据表定义的数据库名称。
     *
     * @return 数据表定义的数据库名称。
     */
    public String getSchemaName() {
        return schemaNameRef.get();
    }

    /**
     * 设置数据表定义的数据库名称。
     *
     * @param schemaName 指定的名称。
     */
    public void setSchemaName(String schemaName) {
        schemaNameRef.set(schemaName);
        cacheMap.clear();
    }

    /**
     * 获取数据表定义的表名称。
     *
     * @return 数据表定义的表名称。
     */
    public String getTableName() {
        return tableNameRef.get();
    }

    /**
     * 设置数据表定义的名称。
     *
     * @param tableName 指定的名称。
     */
    public void setTableName(String tableName) {
        tableNameRef.set(tableName);
        cacheMap.clear();
    }

    /**
     * 查询数据表定义中是否包含指定的列定义。
     *
     * @param name 列定义的名称。
     * @return 是否包含指定的列定义。
     */
    public boolean containsColumnDefinition(String name) {
        return columnDefinitionList.containsKey(name);
    }

    /**
     * 向数据表定义中添加列定义。
     *
     * @param columnDefinition 列定义。
     */
    public void addColumnDefinition(@NonNull ColumnDefinition columnDefinition) {
        columnDefinitionList.add(columnDefinition);
        columnDefinition.setContextTableDefinition(this);
        cacheMap.clear();
    }

    /**
     * 从数据表定义中移除列定义。
     *
     * @param name 列定义的名称。
     */
    public void removeColumnDefinition(String name) {
        ColumnDefinition columnDefinition = columnDefinitionList.get(name);
        columnDefinition.setContextTableDefinition(null);
        columnDefinitionList.remove(columnDefinition);
        cacheMap.clear();
    }

    /**
     * 从数据表定义中获取指定名称的列定义。
     *
     * @param name 指定的名称。
     * @return 指定的名称对应的列定义。
     */
    public ColumnDefinition getColumnDefinition(String name) {
        return columnDefinitionList.get(name);
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return Collections.unmodifiableList(columnDefinitionList);
    }

    public boolean containsOptionalDefinition(String name) {
        return optionalDefinitionList.containsKey(name);
    }

    public void addOptionalDefinition(@NonNull OptionalDefinition optionalDefinition) {
        optionalDefinitionList.add(optionalDefinition);
        optionalDefinition.setContextTableDefinition(this);
        cacheMap.clear();
    }

    public void removeOptionalDefinition(String name) {
        OptionalDefinition optionalDefinition = optionalDefinitionList.get(name);
        optionalDefinition.setContextTableDefinition(null);
        optionalDefinitionList.remove(optionalDefinition);
        cacheMap.clear();
    }

    public OptionalDefinition getOptionalDefinition(String name) {
        return optionalDefinitionList.get(name);
    }

    public List<OptionalDefinition> getOptionalDefinitions() {
        return Collections.unmodifiableList(optionalDefinitionList);
    }

    public List<OptionalDefinition> getOptionalDefinitions(String type) {
        return Collections.unmodifiableList(optionalDefinitionList.stream().filter(
                d -> Objects.equals(type, d.getType())).collect(Collectors.toList()));
    }

    public void putProperty(String key, Object value) {
        propertyMap.put(key, value);
        cacheMap.clear();
    }

    public void putPropertyIfNotNull(String key, Object value) {
        if (Objects.nonNull(value)) {
            propertyMap.put(key, value);
            cacheMap.clear();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void updateProperty(String key, Consumer<Object> vc, Supplier<Object> ifAbsent) {
        Object value;
        if (!propertyMap.containsKey(key)) {
            value = ifAbsent.get();
            propertyMap.put(key, value);
        } else {
            value = propertyMap.get(key);
        }
        vc.accept(value);
        cacheMap.clear();
    }

    public Object getProperty(String key) {
        return propertyMap.get(key);
    }

    public Object getProperty(String key, Object orDefault) {
        return propertyMap.getOrDefault(key, orDefault);
    }

    public boolean hasProperty(String key) {
        return propertyMap.containsKey(key);
    }

    public Object getCache(String key) {
        return cacheMap.get(key);
    }

    public void putCache(String key, Object value) {
        cacheMap.put(key, value);
    }

    public Object getOrPutCache(String key, Object orPut) {
        if (cacheMap.containsKey(key)) {
            return cacheMap.get(key);
        } else {
            cacheMap.put(key, orPut);
            return orPut;
        }
    }

    public boolean hasCache(String key) {
        return cacheMap.containsKey(key);
    }

    public void clearCache() {
        cacheMap.clear();
    }

    @Override
    public String toString() {
        return "TableDefinition{" +
                "schemaNameRef=" + schemaNameRef +
                ", tableNameRef=" + tableNameRef +
                ", columnDefinitionList=" + columnDefinitionList +
                ", optionalDefinitionList=" + optionalDefinitionList +
                ", propertyMap=" + propertyMap +
                ", cacheMap=" + cacheMap +
                '}';
    }
}
