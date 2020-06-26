package com.dwarfeng.subgrade.sdk.jdbc;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface JdbcPresetLookupProcessor<E extends Entity<?>> {

    SQLAndParameter providePresetLookup(String preset, Object[] objs);

    List<E> resolvePresetLookup(ResultSet resultSet) throws SQLException;

    SQLAndParameter providePresetPaging(String preset, Object[] objs, PagingInfo pagingInfo);

    List<E> resolvePresetPaging(ResultSet resultSet) throws SQLException;

    SQLAndParameter providePresetCount(String preset, Object[] objs);

    int resolvePresetCount(ResultSet resultSet) throws SQLException;
}
