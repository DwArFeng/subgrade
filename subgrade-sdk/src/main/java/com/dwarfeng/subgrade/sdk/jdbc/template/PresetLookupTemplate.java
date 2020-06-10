package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

/**
 * 预设查询模板。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface PresetLookupTemplate {

    /**
     * 提供查询用的SQL。
     *
     * @param preset 指定的预设。
     * @param args   指定的参数。
     * @return 查询用的SQL。
     */
    String lookupSQL(String preset, Object[] args);

    /**
     * 提供分页查询用的SQL。
     *
     * @param preset     指定的预设。
     * @param args       指定的参数。
     * @param pagingInfo 指定的分页信息。
     * @return 分页查询用的SQL。
     */
    String pagingSQL(String preset, Object[] args, PagingInfo pagingInfo);

    /**
     * 提供总数查询用的SQL。
     *
     * @param preset 指定的预设。
     * @param args   指定的参数。
     * @return 总数查询用的SQL。
     */
    String countSQL(String preset, Object[] args);
}
