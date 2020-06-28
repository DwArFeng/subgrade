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
 * 可选定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class OptionalDefinition implements WithKey<String> {

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
     * 获取可选定义的名称。
     *
     * @return 可选定义的名称。
     */
    public String getName() {
        return nameRef.get();
    }

    public void setName(String name) {
        nameRef.set(name);
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
    }

    /**
     * 获取可选定义的类型。
     *
     * @return 可选定义的类型。
     */
    public String getType() {
        return typeRef.get();
    }

    public void setType(String type) {
        typeRef.set(type);
        Optional.ofNullable(tableDefinitionRef.get()).ifPresent(TableDefinition::clearCache);
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
        return "OptionalDefinition{" +
                "nameRef=" + nameRef +
                ", typeRef=" + typeRef +
                ", propertyMap=" + propertyMap +
                '}';
    }
}
