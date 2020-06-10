package com.dwarfeng.subgrade.sdk.jdbc.mapper;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果映射器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface ResultMapper<E extends Entity<?>> {

    /**
     * 将结果集合映射为实体。
     *
     * @param resultSet 指定的结果集合。
     * @return 指定的结果映射成的实体。
     * @throws SQLException SQL异常。
     */
    E result2Entity(ResultSet resultSet) throws SQLException;
}
