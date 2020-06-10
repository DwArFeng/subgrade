package com.dwarfeng.subgrade.sdk.jdbc.database;

import com.dwarfeng.subgrade.stack.bean.Bean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * 数据库表定义。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class TableDefinition implements Bean {

    private static final long serialVersionUID = 7430771675523291923L;

    private String schemaName;
    private String tableName;
    private List<ColumnDefinition> columnDefinitions;
    private List<ConstraintDefinition> constraintDefinitions;
    private List<IndexDefinition> indexDefinitions;
    private Map<String, Object> customDefinition;

    public TableDefinition() {
    }

    public TableDefinition(
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

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public List<ConstraintDefinition> getConstraintDefinitions() {
        return constraintDefinitions;
    }

    public void setConstraintDefinitions(List<ConstraintDefinition> constraintDefinitions) {
        this.constraintDefinitions = constraintDefinitions;
    }

    public List<IndexDefinition> getIndexDefinitions() {
        return indexDefinitions;
    }

    public void setIndexDefinitions(List<IndexDefinition> indexDefinitions) {
        this.indexDefinitions = indexDefinitions;
    }

    public Map<String, Object> getCustomDefinition() {
        return customDefinition;
    }

    public void setCustomDefinition(Map<String, Object> customDefinition) {
        this.customDefinition = customDefinition;
    }

    @Override
    public String toString() {
        return "TableDefinition{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columnDefinitions=" + columnDefinitions +
                ", constraintDefinitions=" + constraintDefinitions +
                ", indexDefinitions=" + indexDefinitions +
                ", customDefinition=" + customDefinition +
                '}';
    }

    /**
     * 列定义。
     *
     * @author DwArFeng
     * @since 1.1.0
     */
    public static class ColumnDefinition implements Bean {

        private static final long serialVersionUID = -8852077835867458066L;

        private String name;
        private String type;
        private Map<String, Object> customDefinition;

        public ColumnDefinition() {
        }

        public ColumnDefinition(String name, String type, Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.customDefinition = customDefinition;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "ColumnDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }

    /**
     * 约束定义。
     *
     * @author DwArFeng
     * @since 1.1.0
     */
    public static class ConstraintDefinition implements Bean {

        private static final long serialVersionUID = 8592654160606313624L;

        private String name;
        private String type;
        private List<ColumnDefinition> columnDefinitions;
        private Map<String, Object> customDefinition;

        public ConstraintDefinition() {
        }

        public ConstraintDefinition(
                String name, String type, List<ColumnDefinition> columnDefinitions,
                Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.columnDefinitions = columnDefinitions;
            this.customDefinition = customDefinition;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ColumnDefinition> getColumnDefinitions() {
            return columnDefinitions;
        }

        public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
        }

        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "ConstraintDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", columnDefinitions=" + columnDefinitions +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }

    /**
     * 索引定义。
     *
     * @author DwArFeng
     * @since 1.1.0
     */
    public static class IndexDefinition implements Bean {

        private static final long serialVersionUID = 323860932257378915L;

        private String name;
        private String type;
        private List<ColumnDefinition> columnDefinitions;
        private Map<String, Object> customDefinition;

        public IndexDefinition() {
        }

        public IndexDefinition(
                String name, String type, List<ColumnDefinition> columnDefinitions,
                Map<String, Object> customDefinition) {
            this.name = name;
            this.type = type;
            this.columnDefinitions = columnDefinitions;
            this.customDefinition = customDefinition;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ColumnDefinition> getColumnDefinitions() {
            return columnDefinitions;
        }

        public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
            this.columnDefinitions = columnDefinitions;
        }

        public Map<String, Object> getCustomDefinition() {
            return customDefinition;
        }

        public void setCustomDefinition(Map<String, Object> customDefinition) {
            this.customDefinition = customDefinition;
        }

        @Override
        public String toString() {
            return "IndexDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", columnDefinitions=" + columnDefinitions +
                    ", customDefinition=" + customDefinition +
                    '}';
        }
    }
}
