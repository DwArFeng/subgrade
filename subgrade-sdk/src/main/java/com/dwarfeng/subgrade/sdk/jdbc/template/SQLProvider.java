package com.dwarfeng.subgrade.sdk.jdbc.template;

import java.util.List;

/**
 * 基础SQL提供器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface SQLProvider {

    /**
     * 提供建表用的SQL语句序列。
     *
     * <p>
     * 建表SQL应包括必要的建表语句，必要时包含索引以及外键语句。<br>
     * 该方法是可选的，可以简单的抛出 {@link UnsupportedOperationException} 代表该提供器不支持该方法。
     *
     * @return 建表用的SQL语句序列。
     * @throws UnsupportedOperationException 不支持该方法时抛出的异常。
     */
    List<String> provideCreateTableSQL() throws UnsupportedOperationException;

    /**
     * 提供插入数据用的SQL语句。
     *
     * @return 插入数据用的SQL语句。
     */
    String provideInsertSQL();

    /**
     * 提供更新数据用的SQL语句。
     *
     * @return 更新数据用的SQL语句。
     */
    String provideUpdateSQL();

    /**
     * 提供删除数据用的SQL语句。
     *
     * @return 删除数据用的SQL语句。
     */
    String provideDeleteSQL();

    /**
     * 提供获取数据用的SQL语句。
     *
     * @return 获取数据用的SQL语句。
     */
    String provideGetSQL();

    /**
     * 提供判断数据是否存在用的SQL语句。
     *
     * <p>
     * 该SQL语句需要满足：当数据存在时，返回不少于 1 条结果（什么结果都行）；当数据不存在时，返回 0 条结果。<br>
     * 例如: <code>SELECT 1 FROM table_name WHERE id='specific_value'</code>
     *
     * @return 判断数据是否存在用的SQL语句。
     */
    String provideExistsSQL();
}
