package com.dwarfeng.subgrade.sdk.jdbc.definition;

import java.util.List;
import java.util.Map;

/**
 * 数据表定义。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface TableDefinition {

    /**
     * 获取数据表定义的数据库名称。
     *
     * @return 数据表定义的数据库名称。
     */
    String getSchemaName();

    /**
     * 获取数据表定义的表名称。
     *
     * @return 数据表定义的表名称。
     */
    String getTableName();

    /**
     * 获取数据表定义的列定义。
     *
     * @return 数据表定义的列定义组成的列表。
     */
    List<ColumnDefinition> getColumnDefinitions();

    /**
     * 获取数据表定义的约束定义。
     *
     * @return 数据表定义的约束定义组成的列表。
     */
    List<ConstraintDefinition> getConstraintDefinitions();

    /**
     * 获取数据表定义的索引定义。
     *
     * @return 数据表定义的索引定义组成的列表。
     */
    List<IndexDefinition> getIndexDefinitions();

    /**
     * 获取数据表定义的自定义定义。
     *
     * @return 数据表定义的自定义定义组成的映射。
     */
    Map<String, Object> getCustomDefinition();

    /**
     * 列定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    interface ColumnDefinition {

        /**
         * 获取列定义的名称。
         *
         * @return 列定义的名称。
         */
        String getName();

        /**
         * 获取列定义的类型。
         *
         * @return 列定义的类型。
         */
        String getType();

        /**
         * 获取列定义的自定义定义。
         *
         * @return 列定义的自定义定义组成的映射。
         */
        Map<String, Object> getCustomDefinition();
    }

    /**
     * 约束定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    interface ConstraintDefinition {

        /**
         * 获取约束定义的名称。
         *
         * @return 约束定义的名称。
         */
        String getName();

        /**
         * 获取约束定义的类型。
         *
         * @return 约束定义的类型。
         */
        String getType();

        /**
         * 获取约束定义的的有关列定义。
         *
         * @return 约束定义有关的列定义组成的列表。
         */
        List<ColumnDefinition> getColumnDefinitions();

        /**
         * 获取约束定义的自定义定义。
         *
         * @return 约束定义的自定义定义组成的映射。
         */
        Map<String, Object> getCustomDefinition();
    }

    /**
     * 索引定义。
     *
     * @author DwArFeng
     * @since 1.1.1
     */
    interface IndexDefinition {

        /**
         * 获取索引定义的名称。
         *
         * @return 索引定义的名称。
         */
        String getName();

        /**
         * 获取索引定义的类型。
         *
         * @return 索引定义的类型。
         */
        String getType();

        /**
         * 获取索引定义的的有关列定义。
         *
         * @return 索引定义有关的列定义组成的列表。
         */
        List<ColumnDefinition> getColumnDefinitions();

        /**
         * 获取索引定义的自定义定义。
         *
         * @return 索引定义的自定义定义组成的映射。
         */
        Map<String, Object> getCustomDefinition();
    }
}
