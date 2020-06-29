package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询句柄。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface ResultHandle<E extends Entity<?>> {

    E newInstance();

    void setProperty(E entity, ColumnDefinition columnDefinition, ResultSet resultSet, int index) throws SQLException;
}
