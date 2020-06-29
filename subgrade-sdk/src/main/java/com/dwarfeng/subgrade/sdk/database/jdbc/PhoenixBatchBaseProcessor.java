package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.database.helper.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.jdbc.BatchBaseProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Phoenix 批量基础处理器。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
@SuppressWarnings("DuplicatedCode")
public class PhoenixBatchBaseProcessor<K extends Key, E extends Entity<K>> implements BatchBaseProcessor<K, E> {

    private static final String CACHE_SQL_UPSERT = "CACHE_SQL_UPSERT";
    private static final String CACHE_SQL_DELETE = "CACHE_SQL_DELETE";
    private static final String CACHE_SQL_EXISTS = "CACHE_SQL_EXISTS";
    private static final String CACHE_SQL_GET = "CACHE_SQL_GET";

    private TableDefinition tableDefinition;
    private BaseHandle<K, E> handle;

    public PhoenixBatchBaseProcessor(
            @NonNull TableDefinition tableDefinition, @NonNull BaseHandle<K, E> handle) {
        this.tableDefinition = tableDefinition;
        this.handle = handle;
    }

    @Override
    public SQLAndParameter provideInsert(E element) {
        return provideUpsert(element);
    }

    @Override
    public SQLAndParameter provideUpdate(E element) {
        return provideUpsert(element);
    }

    private SQLAndParameter provideUpsert(E element) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_UPSERT, provideUpsertSQL());
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = handle.getEntityProperty(element, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters, null);
    }

    @Override
    public boolean loopInsert() {
        return false;
    }

    @Override
    public SQLAndParameter provideBatchInsert(List<E> elements) throws UnsupportedOperationException {
        return provideBatchUpsert(elements);
    }

    @Override
    public boolean loopUpdate() {
        return false;
    }

    @Override
    public SQLAndParameter provideBatchUpdate(List<E> elements) throws UnsupportedOperationException {
        return provideBatchUpsert(elements);
    }

    private SQLAndParameter provideBatchUpsert(List<E> elements) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_UPSERT, provideUpsertSQL());
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        List<Object[]> parametersList = new ArrayList<>();
        for (E element : elements) {
            Object[] parameters = new Object[columnDefinitions.size()];
            for (int i = 0; i < columnDefinitions.size(); i++) {
                parameters[i] = handle.getEntityProperty(element, columnDefinitions.get(i));
            }
            parametersList.add(parameters);
        }
        return new SQLAndParameter(sql, null, parametersList);
    }

    private String provideUpsertSQL() {
        return String.format("UPSERT INTO %s(%s) VALUES (%s)",
                SQLUtil.fullTableName(tableDefinition),
                SQLUtil.fullColumnSerial(tableDefinition),
                SQLUtil.fullColumnPlaceHolder(tableDefinition));
    }

    @Override
    public SQLAndParameter provideDelete(K key) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_DELETE, provideDeleteSQL());
        List<ColumnDefinition> columnDefinitions = PhoenixHelper.getPrimaryKeyColumns(tableDefinition);
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = handle.getKeyProperty(key, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters, null);
    }

    @Override
    public boolean loopDelete() {
        return false;
    }

    @Override
    public SQLAndParameter provideBatchDelete(List<K> keys) throws UnsupportedOperationException {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_DELETE, provideDeleteSQL());
        List<ColumnDefinition> columnDefinitions = PhoenixHelper.getPrimaryKeyColumns(tableDefinition);
        List<Object[]> parametersList = new ArrayList<>();
        for (K key : keys) {
            Object[] parameters = new Object[columnDefinitions.size()];
            for (int i = 0; i < columnDefinitions.size(); i++) {
                parameters[i] = handle.getKeyProperty(key, columnDefinitions.get(i));
            }
            parametersList.add(parameters);
        }
        return new SQLAndParameter(sql, null, parametersList);
    }

    private String provideDeleteSQL() {
        return String.format("DELETE FROM %s WHERE %s",
                SQLUtil.fullTableName(tableDefinition),
                SQLUtil.searchFragment(PhoenixHelper.getPrimaryKeyColumns(tableDefinition)));
    }

    @Override
    public SQLAndParameter provideExists(K key) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_EXISTS, provideExistsSQL());
        List<ColumnDefinition> columnDefinitions = PhoenixHelper.getPrimaryKeyColumns(tableDefinition);
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = handle.getKeyProperty(key, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters, null);
    }

    @Override
    public boolean resolveExists(ResultSet resultSet) throws SQLException {
        return resultSet.next();
    }

    @Override
    public boolean loopExists() {
        return true;
    }

    @Override
    public SQLAndParameter provideAllExists(List<K> keys) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("provideAllExists");
    }

    @Override
    public boolean resolveAllExists(ResultSet resultSet) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("resolveAllExists");
    }

    @Override
    public SQLAndParameter provideNonExists(List<K> keys) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("provideNonExists");
    }

    @Override
    public boolean resolveNonExists(ResultSet resultSet) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("resolveNonExists");
    }

    private String provideExistsSQL() {
        return String.format("SELECT 1 FROM %s WHERE %s",
                SQLUtil.fullTableName(tableDefinition),
                SQLUtil.searchFragment(PhoenixHelper.getPrimaryKeyColumns(tableDefinition)));
    }

    @Override
    public SQLAndParameter provideGet(K key) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_GET, provideGetSQL());
        List<ColumnDefinition> columnDefinitions = PhoenixHelper.getPrimaryKeyColumns(tableDefinition);
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = handle.getKeyProperty(key, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters, null);
    }

    @Override
    public E resolveGet(ResultSet resultSet) throws SQLException {
        resultSet.next();
        E entity = handle.newInstance();
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        for (int i = 0; i < columnDefinitions.size(); i++) {
            handle.setProperty(entity, columnDefinitions.get(i), resultSet, i + 1);
        }
        return entity;
    }

    @Override
    public boolean loopGet() {
        return true;
    }

    @Override
    public SQLAndParameter provideBatchGet(List<K> keys) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("provideBatchGet");
    }

    @Override
    public List<E> resolveBatchGet(ResultSet resultSet) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("resolveBatchGet");
    }

    private String provideGetSQL() {
        return String.format("SELECT %s FROM %s WHERE %s",
                SQLUtil.fullColumnSerial(tableDefinition),
                SQLUtil.fullTableName(tableDefinition),
                SQLUtil.searchFragment(PhoenixHelper.getPrimaryKeyColumns(tableDefinition)));
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public BaseHandle<K, E> getHandle() {
        return handle;
    }

    public void setHandle(@NonNull BaseHandle<K, E> handle) {
        this.handle = handle;
    }
}
