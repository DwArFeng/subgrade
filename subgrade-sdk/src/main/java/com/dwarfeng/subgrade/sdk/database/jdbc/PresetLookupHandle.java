package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * 预设查询句柄。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface PresetLookupHandle<E extends Entity<?>> extends ResultHandle<E> {

    QueryInfo getQueryInfo(String preset, Object[] args);
}
