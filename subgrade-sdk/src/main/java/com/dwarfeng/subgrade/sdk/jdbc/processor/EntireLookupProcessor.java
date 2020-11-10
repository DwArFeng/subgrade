package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface EntireLookupProcessor<E extends Entity<?>> {

    SQLAndParameter provideEntireLookup();

    List<E> resolveEntireLookup(ResultSet resultSet) throws SQLException;

    SQLAndParameter provideEntirePaging(PagingInfo pagingInfo);

    List<E> resolveEntirePaging(ResultSet resultSet) throws SQLException;

    SQLAndParameter provideEntireCount();

    int resolveEntireCount(ResultSet resultSet) throws SQLException;
}
