package com.dwarfeng.subgrade.sdk.jdbc.handle;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface BaseHandle<K extends Key, E extends Entity<K>> extends ResultHandle<E>, WriteHandle<E> {

    Object getKeyProperty(K key, ColumnDefinition columnDefinition);
}
