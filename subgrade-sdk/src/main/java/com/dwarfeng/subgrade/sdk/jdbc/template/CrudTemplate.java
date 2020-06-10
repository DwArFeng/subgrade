package com.dwarfeng.subgrade.sdk.jdbc.template;

/**
 * Crud模板。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface CrudTemplate {

    /**
     * 提供插入数据用的SQL语句。
     *
     * @return 插入数据用的SQL语句。
     */
    String insertSQL();

    /**
     * 提供更新数据用的SQL语句。
     *
     * @return 更新数据用的SQL语句。
     */
    String updateSQL();

    /**
     * 提供删除数据用的SQL语句。
     *
     * @return 删除数据用的SQL语句。
     */
    String deleteSQL();

    /**
     * 提供获取数据用的SQL语句。
     *
     * @return 获取数据用的SQL语句。
     */
    String getSQL();

    /**
     * 提供判断数据是否存在用的SQL语句。
     *
     * <p>
     * 该SQL语句需要满足：当数据存在时，返回不少于 1 条结果（什么结果都行）；当数据不存在时，返回 0 条结果。<br>
     * 例如: <code>SELECT 1 FROM table_name WHERE id='specific_value'</code>
     *
     * @return 判断数据是否存在用的SQL语句。
     */
    String existsSQL();
}
