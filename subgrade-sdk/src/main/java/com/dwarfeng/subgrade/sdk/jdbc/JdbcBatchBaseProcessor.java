package com.dwarfeng.subgrade.sdk.jdbc;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface JdbcBatchBaseProcessor<K extends Key, E extends Entity<K>> extends JdbcBaseProcessor<K, E> {

    boolean loopInsert();

    SQLAndParameter provideBatchInsert(List<E> elements) throws UnsupportedOperationException;

    boolean loopUpdate();

    SQLAndParameter provideBatchUpdate(List<E> elements) throws UnsupportedOperationException;

    boolean loopDelete();

    SQLAndParameter provideBatchDelete(List<K> keys) throws UnsupportedOperationException;

    boolean loopExists();

    SQLAndParameter provideAllExists(List<K> keys) throws UnsupportedOperationException;

    boolean resolveAllExists(ResultSet resultSet) throws SQLException, UnsupportedOperationException;

    SQLAndParameter provideNonExists(List<K> keys) throws UnsupportedOperationException;

    boolean resolveNonExists(ResultSet resultSet) throws SQLException, UnsupportedOperationException;

    boolean loopGet();

    SQLAndParameter provideBatchGet(List<K> keys) throws UnsupportedOperationException;

    List<E> resolveBatchGet(ResultSet resultSet) throws SQLException, UnsupportedOperationException;
}
