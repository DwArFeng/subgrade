package com.dwarfeng.subgrade.sdk.jdbc.handle;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * 基础句柄。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface WriteHandle<E extends Entity<?>> {

    Object getEntityProperty(E entity, ColumnDefinition columnDefinition);
}
