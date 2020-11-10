package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.handle.ResultHandle;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Phoenix 全体查询处理器。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class PhoenixEntireLookupProcessor<E extends Entity<?>> implements EntireLookupProcessor<E> {

    private static final String CACHE_SQL_ENTIRE_LOOKUP = "CACHE_SQL_ENTIRE_LOOKUP";
    private static final String CACHE_SQL_ENTIRE_PAGING = "CACHE_SQL_ENTIRE_PAGING";
    private static final String CACHE_SQL_ENTIRE_COUNT = "CACHE_SQL_ENTIRE_COUNT";

    private TableDefinition tableDefinition;
    private ResultHandle<E> handle;

    public PhoenixEntireLookupProcessor(
            @NonNull TableDefinition tableDefinition, @NonNull ResultHandle<E> resultHandle) {
        this.tableDefinition = tableDefinition;
        this.handle = resultHandle;
    }

    @Override
    public SQLAndParameter provideEntireLookup() {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_ENTIRE_LOOKUP, provideEntireLookupSQL());
        Object[] parameters = new Object[0];
        return new SQLAndParameter(sql, parameters);
    }

    @Override
    public List<E> resolveEntireLookup(ResultSet resultSet) throws SQLException {
        return resolveEntity(resultSet);
    }

    @Override
    public SQLAndParameter provideEntirePaging(PagingInfo pagingInfo) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_ENTIRE_PAGING, provideEntirePagingSQL());
        Object[] parameters = new Object[]{
                Math.max(0, pagingInfo.getRows()), Math.max(0, pagingInfo.getRows() * pagingInfo.getPage())};
        return new SQLAndParameter(sql, parameters);
    }

    @Override
    public List<E> resolveEntirePaging(ResultSet resultSet) throws SQLException {
        return resolveEntity(resultSet);
    }

    @Override
    public SQLAndParameter provideEntireCount() {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_ENTIRE_COUNT, provideEntireCountSQL());
        Object[] parameters = new Object[0];
        return new SQLAndParameter(sql, parameters);
    }

    @Override
    public int resolveEntireCount(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return new Long(resultSet.getLong(1)).intValue();
    }

    private String provideEntireLookupSQL() {
        return String.format("SELECT %s FROM %s",
                SQLUtil.fullColumnSerial(tableDefinition),
                PhoenixHelper.getFullTableName(tableDefinition));
    }

    private String provideEntirePagingSQL() {
        return String.format("SELECT %s FROM %s LIMIT ? OFFSET ?",
                SQLUtil.fullColumnSerial(tableDefinition),
                PhoenixHelper.getFullTableName(tableDefinition));
    }

    private String provideEntireCountSQL() {
        return String.format("SELECT COUNT(%s) FROM %s",
                SQLUtil.columnSerial(PhoenixHelper.getPrimaryKeyColumns(tableDefinition)),
                PhoenixHelper.getFullTableName(tableDefinition));
    }

    @SuppressWarnings("DuplicatedCode")
    private List<E> resolveEntity(ResultSet resultSet) throws SQLException {
        List<E> entities = new ArrayList<>();
        while (resultSet.next()) {
            E entity = handle.newInstance();
            List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
            for (int i = 0; i < columnDefinitions.size(); i++) {
                handle.setProperty(entity, columnDefinitions.get(i), resultSet, i + 1);
            }
            entities.add(entity);
        }
        return entities;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public ResultHandle<E> getHandle() {
        return handle;
    }

    public void setHandle(@NonNull ResultHandle<E> handle) {
        this.handle = handle;
    }
}
