package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 基础处理器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface BaseProcessor<K extends Key, E extends Entity<K>> {

    SQLAndParameter provideInsert(E element);

    SQLAndParameter provideUpdate(E element);

    SQLAndParameter provideDelete(K key);

    SQLAndParameter provideExists(K key);

    boolean resolveExists(ResultSet resultSet) throws SQLException;

    SQLAndParameter provideGet(K key);

    E resolveGet(ResultSet resultSet) throws SQLException;
}
