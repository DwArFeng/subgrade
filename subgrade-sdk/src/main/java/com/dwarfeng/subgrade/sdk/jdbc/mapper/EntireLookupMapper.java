package com.dwarfeng.subgrade.sdk.jdbc.mapper;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

/**
 * 预设查询映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface EntireLookupMapper {

    /**
     * 提供查询用的参数数组。
     *
     * @return 查询用的参数数组。
     */
    Object[] lookup2Args();

    /**
     * 提供分页查询用的参数数组。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 分页查询用的参数数组。
     */
    Object[] paging2Args(PagingInfo pagingInfo);

    /**
     * 提供总数查询用的参数数组。
     *
     * @return 总数查询用的参数数组。
     */
    Object[] count2Args();
}
