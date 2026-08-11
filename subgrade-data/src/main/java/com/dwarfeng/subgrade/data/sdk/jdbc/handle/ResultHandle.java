package com.dwarfeng.subgrade.data.sdk.jdbc.handle;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.data.sdk.database.definition.ColumnDefinition;

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
