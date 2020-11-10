package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.BatchWriteProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Phoenix 批量写入处理器。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
@SuppressWarnings("DuplicatedCode")
public class PhoenixBatchWriteProcessor<E extends Entity<?>> implements BatchWriteProcessor<E> {

    private static final String CACHE_SQL_WRITE = "CACHE_SQL_WRITE";

    private TableDefinition tableDefinition;
    private WriteHandle<E> handle;

    public PhoenixBatchWriteProcessor(
            @NonNull TableDefinition tableDefinition, @NonNull WriteHandle<E> handle) {
        this.tableDefinition = tableDefinition;
        this.handle = handle;
    }

    @Override
    public SQLAndParameter provideWrite(E element) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_WRITE, provideWriteSQL());
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = handle.getEntityProperty(element, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters);
    }

    @Override
    public boolean loopWrite() {
        return false;
    }

    @Override
    public SQLAndParameter provideBatchWrite(List<E> elements) throws UnsupportedOperationException {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_WRITE, provideWriteSQL());
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        List<Object[]> parametersList = new ArrayList<>();
        for (E element : elements) {
            Object[] parameters = new Object[columnDefinitions.size()];
            for (int i = 0; i < columnDefinitions.size(); i++) {
                parameters[i] = handle.getEntityProperty(element, columnDefinitions.get(i));
            }
            parametersList.add(parameters);
        }
        return new SQLAndParameter(sql, parametersList);
    }

    private String provideWriteSQL() {
        return String.format("UPSERT INTO %s(%s) VALUES (%s)",
                PhoenixHelper.getFullTableName(tableDefinition),
                SQLUtil.fullColumnSerial(tableDefinition),
                SQLUtil.fullColumnPlaceHolder(tableDefinition));
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public WriteHandle<E> getHandle() {
        return handle;
    }

    public void setHandle(@NonNull WriteHandle<E> handle) {
        this.handle = handle;
    }
}
