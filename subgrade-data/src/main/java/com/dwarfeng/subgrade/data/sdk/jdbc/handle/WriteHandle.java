package com.dwarfeng.subgrade.data.sdk.jdbc.handle;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.database.definition.ColumnDefinition;

/**
 * 基础句柄。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface WriteHandle<E extends Entity<?>> {

    Object getEntityProperty(E entity, ColumnDefinition columnDefinition);
}
