package com.dwarfeng.subgrade.sdk.jdbc.definition;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * 默认数据库表定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class DefaultTableDefinition implements TableDefinition {

    private String schemaName;
    private String tableName;
    private List<ColumnDefinition> columnDefinitions;
    private List<ConstraintDefinition> constraintDefinitions;
    private List<IndexDefinition> indexDefinitions;
    private Map<String, Object> customDefinition;

    public DefaultTableDefinition() {
    }

    public DefaultTableDefinition(
            String schemaName,
            @NonNull String tableName,
            @NonNull List<ColumnDefinition> columnDefinitions,
            @NonNull List<ConstraintDefinition> constraintDefinitions,
            @NonNull List<IndexDefinition> indexDefinitions,
            @NonNull Map<String, Object> customDefinition) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnDefinitions = columnDefinitions;
        this.constraintDefinitions = constraintDefinitions;
        this.indexDefinitions = indexDefinitions;
        this.customDefinition = customDefinition;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    @Override
    public List<ConstraintDefinition> getConstraintDefinitions() {
        return constraintDefinitions;
    }

    public void setConstraintDefinitions(List<ConstraintDefinition> constraintDefinitions) {
        this.constraintDefinitions = constraintDefinitions;
    }

    @Override
    public List<IndexDefinition> getIndexDefinitions() {
        return indexDefinitions;
    }

    public void setIndexDefinitions(List<IndexDefinition> indexDefinitions) {
        this.indexDefinitions = indexDefinitions;
    }

    @Override
    public Map<String, Object> getCustomDefinition() {
        return customDefinition;
    }

    public void setCustomDefinition(Map<String, Object> customDefinition) {
        this.customDefinition = customDefinition;
    }

    @Override
    public String toString() {
        return "DefaultTableDefinition{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnDefinitions=" + columnDefinitions +
                ", constraintDefinitions=" + constraintDefinitions +
                ", indexDefinitions=" + indexDefinitions +
                ", customDefinition=" + customDefinition +
                '}';
    }

    /**
     * 默认列定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    public static class DefaultColumnDefinition implements ColumnDefinition {

        private String name;
        private String type;
        private Map<String, Object> customDefinition;

        public DefaultColumnDefinition() {
        }

        public DefaultColumnDefinition(String name, String type, Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.customDefinition = customDefinition;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "DefaultColumnDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }

    /**
     * 默认约束定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    public static class DefaultConstraintDefinition implements ConstraintDefinition {

        private String name;
        private String type;
        private List<ColumnDefinition> columnDefinitions;
        private Map<String, Object> customDefinition;

        public DefaultConstraintDefinition() {
        }

        public DefaultConstraintDefinition(
                String name, String type, List<ColumnDefinition> columnDefinitions,
                Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.columnDefinitions = columnDefinitions;
            this.customDefinition = customDefinition;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public List<ColumnDefinition> getColumnDefinitions() {
            return columnDefinitions;
        }

        public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
        }

        @Override
        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "DefaultConstraintDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", columnDefinitions=" + columnDefinitions +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }

    /**
     * 默认索引定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    public static class DefaultIndexDefinition implements IndexDefinition {

        private String name;
        private String type;
        private List<ColumnDefinition> columnDefinitions;
        private Map<String, Object> customDefinition;

        public DefaultIndexDefinition() {
        }

        public DefaultIndexDefinition(
                String name, String type, List<ColumnDefinition> columnDefinitions,
                Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.columnDefinitions = columnDefinitions;
            this.customDefinition = customDefinition;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public List<ColumnDefinition> getColumnDefinitions() {
            return columnDefinitions;
        }

        public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
        }

        @Override
        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "DefaultIndexDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", columnDefinitions=" + columnDefinitions +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }
}
