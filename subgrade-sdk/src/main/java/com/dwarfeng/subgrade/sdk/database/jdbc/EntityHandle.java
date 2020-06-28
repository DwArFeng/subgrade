package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface EntityHandle<K extends Key, E extends Entity<K>> {

    E newInstance();

    Object getKeyProperty(K key, ColumnDefinition columnDefinition);

    Object getEntityProperty(E entity, ColumnDefinition columnDefinition);

    void setProperty(
            E entity, ColumnDefinition columnDefinition, ResultSet resultSet, int index) throws SQLException;
}
