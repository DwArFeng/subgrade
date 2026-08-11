package com.dwarfeng.subgrade.data.sdk.jdbc.handle;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;

/**
 * 预设查询句柄。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface PresetLookupHandle<E extends Entity<?>> extends ResultHandle<E> {

    QueryInfo getQueryInfo(String preset, Object[] args);
}
