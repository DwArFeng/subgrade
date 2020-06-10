package com.dwarfeng.subgrade.sdk.jdbc.template;

import java.util.List;

/**
 * 建表模板。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface CreateTableTemplate {

    /**
     * 提供建表用的SQL语句序列。
     *
     * <p>
     * 建表SQL应包括必要的建表语句，必要时包含索引以及外键语句。<br>
     *
     * @return 建表用的SQL语句序列。
     */
    List<String> createTableSQL();
}
