package com.dwarfeng.subgrade.sdk.jdbc.mapper;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

/**
 * 预设查询映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface PresetLookupMapper {

    /**
     * 提供查询用的参数数组。
     *
     * @param preset 指定的预设。
     * @param args   指定的参数。
     * @return 查询用的参数数组。
     */
    Object[] lookup2Args(String preset, Object[] args);

    /**
     * 提供分页查询用的参数数组。
     *
     * @param preset     指定的预设。
     * @param args       指定的参数。
     * @param pagingInfo 指定的分页信息。
     * @return 分页查询用的参数数组。
     */
    Object[] paging2Args(String preset, Object[] args, PagingInfo pagingInfo);

    /**
     * 提供总数查询用的参数数组。
     *
     * @param preset 指定的预设。
     * @param args   指定的参数。
     * @return 总数查询用的参数数组。
     */
    Object[] count2Args(String preset, Object[] args);
}
