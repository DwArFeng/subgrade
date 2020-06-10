package com.dwarfeng.subgrade.sdk.jdbc.mapper;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

/**
 * Phoenix 预设查询映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixEntireLookupMapper implements EntireLookupMapper {

    @Override
    public Object[] lookup2Args() {
        return new Object[0];
    }

    @Override
    public Object[] paging2Args(PagingInfo pagingInfo) {
        return new Object[]{Math.max(pagingInfo.getPage() * pagingInfo.getRows(), 0), Math.max(pagingInfo.getRows(), 0)};
    }

    @Override
    public Object[] count2Args() {
        return new Object[0];
    }
}
