package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.BatchWriteProcessor;
import com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
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
public class PhoenixBatchWriteProcessor<K extends Key, E extends Entity<K>> implements BatchWriteProcessor<E> {

    private static final String CACHE_SQL_WRITE = "CACHE_SQL_WRITE";

    private TableDefinition tableDefinition;
    private EntityHandle<K, E> entityHandle;

    public PhoenixBatchWriteProcessor(
            @NonNull TableDefinition tableDefinition, @NonNull EntityHandle<K, E> entityHandle) {
        this.tableDefinition = tableDefinition;
        this.entityHandle = entityHandle;
    }

    @Override
    public SQLAndParameter provideWrite(E element) {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_WRITE, provideWriteSQL());
        List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
        Object[] parameters = new Object[columnDefinitions.size()];
        for (int i = 0; i < columnDefinitions.size(); i++) {
            parameters[i] = entityHandle.getEntityProperty(element, columnDefinitions.get(i));
        }
        return new SQLAndParameter(sql, parameters, null);
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
                parameters[i] = entityHandle.getEntityProperty(element, columnDefinitions.get(i));
            }
            parametersList.add(parameters);
        }
        return new SQLAndParameter(sql, null, parametersList);
    }

    private String provideWriteSQL() {
        return String.format("UPSERT INTO %s(%s) VALUES (%s)",
                SQLUtil.fullTableName(tableDefinition),
                SQLUtil.fullColumnSerial(tableDefinition),
                SQLUtil.fullColumnPlaceHolder(tableDefinition));
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public EntityHandle<K, E> getEntityHandle() {
        return entityHandle;
    }

    public void setEntityHandle(@NonNull EntityHandle<K, E> entityHandle) {
        this.entityHandle = entityHandle;
    }
}
