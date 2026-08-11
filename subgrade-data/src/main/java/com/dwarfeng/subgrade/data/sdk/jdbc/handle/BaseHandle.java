package com.dwarfeng.subgrade.data.sdk.jdbc.handle;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.sdk.database.definition.ColumnDefinition;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface BaseHandle<K extends Key, E extends Entity<K>> extends ResultHandle<E>, WriteHandle<E> {

    Object getKeyProperty(K key, ColumnDefinition columnDefinition);
}
