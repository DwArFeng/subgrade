package com.dwarfeng.subgrade.sdk.jdbc.template;

/**
 * 整体查询模板。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface EntireLookupTemplate {

    /**
     * 提供查询用的SQL。
     *
     * @return 查询用的SQL。
     */
    String lookupSQL();

    /**
     * 提供分页查询用的SQL。
     *
     * @return 分页查询用的SQL。
     */
    String pagingSQL();

    /**
     * 提供总数查询用的SQL。
     *
     * @return 总数查询用的SQL。
     */
    String countSQL();
}
