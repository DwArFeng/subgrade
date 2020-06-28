package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;

import java.util.List;
import java.util.Objects;

/**
 * SQL工具类。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public final class SQLUtil {

    /**
     * 获取指定的数据库表定义对应的全序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT <u>id, name, age</u> FROM t</code><br>
     * <code>INSERT INTO t (<u>id, name, age</u>) VALUES (?, ?, ?)</code>
     *
     * @param tableDefinition 指定的数据库表定义。
     * @return 全序列对应的SQL语句。
     */
    public static String fullColumnSerial(TableDefinition tableDefinition) {
        return columnSerial(tableDefinition.getColumnDefinitions(), "");
    }

    /**
     * 获取指定的数据库表定义对应的全序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT <u>t.id, t.name, t.age</u> FROM table AS t</code><br>
     *
     * @param tableDefinition 指定的数据库表定义。
     * @param prefix          列名称的前缀。
     * @return 全序列对应的SQL语句。
     */
    public static String fullColumnSerial(TableDefinition tableDefinition, String prefix) {
        return columnSerial(tableDefinition.getColumnDefinitions(), prefix);
    }

    /**
     * 获取指定的列定义对应的序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT <u>id, name, age</u> FROM t</code><br>
     * <code>INSERT INTO t (<u>id, name, age</u>) VALUES (?, ?, ?)</code>
     *
     * @param columnDefinitions 指定的列定义集合。
     * @return 序列对应的SQL语句。
     */
    public static String columnSerial(List<ColumnDefinition> columnDefinitions) {
        return columnSerial(columnDefinitions, "");
    }

    /**
     * 获取指定的列定义对应的序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT <u>t.id, t.name, t.age</u> FROM table AS t</code><br>
     *
     * @param columnDefinitions 指定的列定义集合。
     * @param prefix            列名称的前缀。
     * @return 序列对应的SQL语句。
     */
    public static String columnSerial(List<ColumnDefinition> columnDefinitions, String prefix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnDefinitions.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(prefix).append(columnDefinitions.get(i).getName());
        }
        return sb.toString();
    }

    /**
     * 获取指定的数据库表定义对应的全占位符对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>INSERT INTO t (id, name, age) VALUES (<u>?, ?, ?</u>)</code>
     *
     * @param tableDefinition 指定的数据库表定义。
     * @return 全占位符对应的SQL语句。
     */
    public static String fullColumnPlaceHolder(TableDefinition tableDefinition) {
        return placeHolder(tableDefinition.getColumnDefinitions().size());
    }

    /**
     * 获取指定的列定义对应的占位符对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>INSERT INTO t (id, name, age) VALUES (<u>?, ?, ?</u>)</code>
     *
     * @param columnDefinitions 指定的列定义集合。
     * @return 占位符对应的SQL语句。
     */
    public static String columnPlaceHolder(List<ColumnDefinition> columnDefinitions) {
        return placeHolder(columnDefinitions.size());
    }

    /**
     * 获取指定数量的占位符对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>INSERT INTO t (id, name, age) VALUES (<u>?, ?, ?</u>)</code>
     *
     * @param size 指定的数量。
     * @return 占位符对应的SQL语句。
     */
    public static String placeHolder(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                sb.append("?");
            } else {
                sb.append(", ?");
            }
        }
        return sb.toString();
    }

    /**
     * 获取指定的数据库表定义对应的全表更新对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>UPDATE t SET <u>name = ?, age = ?</u> FROM t WHERE id = ?</code><br>
     *
     * @param tableDefinition 指定的数据库表定义。
     * @return 全表更新对应的SQL语句。
     */
    public static String fullUpdateFragment(TableDefinition tableDefinition) {
        return updateFragment(tableDefinition.getColumnDefinitions());
    }

    /**
     * 获取指定的列定义对应的表更新对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>UPDATE t SET <u>name = ?, age = ?</u> FROM t WHERE id = ?</code><br>
     *
     * @param columnDefinitions 指定的列定义集合。
     * @return 表更新对应的SQL语句。
     */
    public static String updateFragment(List<ColumnDefinition> columnDefinitions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            if (i == 0) {
                sb.append(columnDefinition.getName()).append(" = ?");
            } else {
                sb.append(", ").append(columnDefinition.getName()).append(" = ?");
            }
        }
        return sb.toString();
    }

    /**
     * 获取指定的列定义对应的主键搜索对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT id, country, name, age FROM t WHERE <u>id = ? AND country = ?</u></code><br>
     * <code>UPDATE t SET name = ?, age = ? FROM t WHERE <u>id = ? AND country = ?</u></code><br>
     *
     * @param columnDefinitions 指定的列定义。
     * @return 主键搜索对应的SQL语句。
     */
    public static String searchFragment(List<ColumnDefinition> columnDefinitions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            if (i == 0) {
                sb.append(columnDefinition.getName()).append(" = ?");
            } else {
                sb.append(" AND ").append(columnDefinition.getName()).append(" = ?");
            }
        }
        return sb.toString();
    }

    /**
     * 获取指定的数据库表定义对应的全表名称。
     *
     * @param tableDefinition 指定的数据库表定义。
     * @return 全表名称。
     */
    public static String fullTableName(TableDefinition tableDefinition) {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(tableDefinition.getSchemaName())) {
            sb.append(tableDefinition.getSchemaName()).append('.');
        }
        sb.append(tableDefinition.getTableName());
        return sb.toString();
    }
}
