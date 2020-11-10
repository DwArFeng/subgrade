package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.handle.PresetLookupHandle;
import com.dwarfeng.subgrade.sdk.jdbc.handle.QueryInfo;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Phoenix 预设查询处理器。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class PhoenixPresetLookupProcessor<E extends Entity<?>> implements PresetLookupProcessor<E> {

    private static final String CACHE_FRAGMENT_PRESET_LOOKUP = "CACHE_FRAGMENT_PRESET_LOOKUP";
    private static final String CACHE_FRAGMENT_PRESET_PAGING = "CACHE_FRAGMENT_PRESET_PAGING";
    private static final String CACHE_FRAGMENT_PRESET_COUNT = "CACHE_FRAGMENT_PRESET_COUNT";

    private TableDefinition tableDefinition;
    private PresetLookupHandle<E> handle;

    public PhoenixPresetLookupProcessor(
            @NonNull TableDefinition tableDefinition, @NonNull PresetLookupHandle<E> handle) {
        this.tableDefinition = tableDefinition;
        this.handle = handle;
    }

    @Override
    public SQLAndParameter providePresetLookup(String preset, Object[] objs) {
        String lookupFragment = (String) tableDefinition.getOrPutCache(CACHE_FRAGMENT_PRESET_LOOKUP,
                providePresetLookupFragment());
        QueryInfo queryInfo = handle.getQueryInfo(preset, objs);
        StringBuilder whereClause = whereClause(queryInfo);
        StringBuilder orderClause = orderClause(queryInfo);
        String sql = lookupFragment + whereClause.toString() + orderClause.toString();
        return new SQLAndParameter(sql, queryInfo.getParameters());
    }

    @Override
    public List<E> resolvePresetLookup(ResultSet resultSet) throws SQLException {
        return resolveEntity(resultSet);
    }

    @Override
    public SQLAndParameter providePresetPaging(String preset, Object[] objs, PagingInfo pagingInfo) {
        String lookupFragment = (String) tableDefinition.getOrPutCache(CACHE_FRAGMENT_PRESET_LOOKUP,
                providePresetLookupFragment());
        String pagingFragment = (String) tableDefinition.getOrPutCache(CACHE_FRAGMENT_PRESET_PAGING,
                providePresetPagingFragment());
        QueryInfo queryInfo = handle.getQueryInfo(preset, objs);
        StringBuilder whereClause = whereClause(queryInfo);
        StringBuilder orderClause = orderClause(queryInfo);
        String sql = lookupFragment + whereClause.toString() + orderClause.toString() + " " + pagingFragment;
        Object[] parameters = new Object[queryInfo.getParameters().length + 2];
        System.arraycopy(queryInfo.getParameters(), 0, parameters, 0, queryInfo.getParameters().length);
        parameters[parameters.length - 2] = Math.max(0, pagingInfo.getRows());
        parameters[parameters.length - 1] = Math.max(0, pagingInfo.getRows() * pagingInfo.getPage());
        return new SQLAndParameter(sql, parameters);
    }

    @Override
    public List<E> resolvePresetPaging(ResultSet resultSet) throws SQLException {
        return resolveEntity(resultSet);
    }

    @Override
    public SQLAndParameter providePresetCount(String preset, Object[] objs) {
        String countFragment = (String) tableDefinition.getOrPutCache(CACHE_FRAGMENT_PRESET_COUNT,
                providePresetCountFragment());
        QueryInfo queryInfo = handle.getQueryInfo(preset, objs);
        StringBuilder whereClause = whereClause(queryInfo);
        StringBuilder orderClause = orderClause(queryInfo);
        String sql = countFragment + whereClause.toString() + orderClause.toString();
        return new SQLAndParameter(sql, queryInfo.getParameters());
    }

    @Override
    public int resolvePresetCount(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return new Long(resultSet.getLong(1)).intValue();
    }

    private String providePresetLookupFragment() {
        return String.format("SELECT %s FROM %s",
                SQLUtil.fullColumnSerial(tableDefinition),
                PhoenixHelper.getFullTableName(tableDefinition));
    }

    private String providePresetPagingFragment() {
        return "LIMIT ? OFFSET ?";
    }

    private String providePresetCountFragment() {
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

    private StringBuilder whereClause(QueryInfo queryInfo) {
        StringBuilder whereClause = new StringBuilder();
        if (Objects.nonNull(queryInfo.getWhereClause())) {
            whereClause.append(" WHERE ");
            whereClause.append(queryInfo.getWhereClause());
        }
        return whereClause;
    }

    private StringBuilder orderClause(QueryInfo queryInfo) {
        StringBuilder orderClause = new StringBuilder();
        if (Objects.nonNull(queryInfo.getOrderingMap()) && !queryInfo.getOrderingMap().isEmpty()) {
            orderClause = new StringBuilder(" ORDER BY ");
            int i = 0;
            for (Map.Entry<String, QueryInfo.Ordering> entry : queryInfo.getOrderingMap().entrySet()) {
                if (i++ != 0) {
                    orderClause.append(", ");
                }
                orderClause.append(entry.getKey()).append(" ");
                switch (entry.getValue()) {
                    case ASC:
                        orderClause.append("ASC");
                        break;
                    case DESC:
                        orderClause.append("DESC");
                        break;
                }
            }
        }
        return orderClause;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public PresetLookupHandle<E> getHandle() {
        return handle;
    }

    public void setHandle(@NonNull PresetLookupHandle<E> handle) {
        this.handle = handle;
    }
}
