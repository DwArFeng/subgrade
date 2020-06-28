package com.dwarfeng.subgrade.sdk.database.definition;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateMapModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.WithKey;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 列定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class ColumnDefinition implements WithKey<String> {

    private final ReferenceModel<TableDefinition> tableDefinitionRef = new DefaultReferenceModel<>();
    private final ReferenceModel<String> nameRef = new DefaultReferenceModel<>();
    private final ReferenceModel<String> typeRef = new DefaultReferenceModel<>();
    private final MapModel<String, Object> propertyMap = new DelegateMapModel<>();

    @Override
    public String getKey() {
        return getName();
    }

    public TableDefinition getContextTableDefinition() {
        return tableDefinitionRef.get();
    }

    public void setContextTableDefinition(TableDefinition contextTableDefinition) {
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
        tableDefinitionRef.set(contextTableDefinition);
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
    }

    /**
     * 获取列定义的名称。
     *
     * @return 列定义的名称。
     */
    public String getName() {
        return nameRef.get();
    }

    public void setName(String name) {
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
        nameRef.set(name);
    }

    /**
     * 获取列定义的类型。
     *
     * @return 列定义的类型。
     */
    public String getType() {
        return typeRef.get();
    }

    public void setType(String type) {
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
        typeRef.set(type);
    }

    public void putProperty(String key, Object value) {
        propertyMap.put(key, value);
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
    }

    public void putPropertyIfNotNull(String key, Object value) {
        if (Objects.nonNull(value)) {
            propertyMap.put(key, value);
            Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
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
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
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

    @Override
    public String toString() {
        return "ColumnDefinition{" +
                "nameRef=" + nameRef +
                ", typeRef=" + typeRef +
                ", propertyMap=" + propertyMap +
                '}';
    }
}
