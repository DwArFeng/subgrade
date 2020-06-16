package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixUtil;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinitionUtil;
import org.springframework.lang.NonNull;

/**
 * Phoenix Crud模板。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixCrudTemplate extends GeneralCrudTemplate {

    public PhoenixCrudTemplate(@NonNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    protected String internalInsertSQL() {
        return provideUpsertSQL();
    }

    @Override
    protected String internalUpdateSQL() {
        return provideUpsertSQL();
    }

    private String provideUpsertSQL() {
        return String.format(
                "UPSERT INTO %s (%s) VALUES (%s)",
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.fullColumnSerial(tableDefinition),
                TableDefinitionUtil.fullColumnPlaceHolder(tableDefinition));
    }

    @Override
    protected String internalDeleteSQL() {
        return String.format(
                "DELETE FROM %s WHERE %s",
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.searchFragment(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)));
    }

    @Override
    protected String internalGetSQL() {
        return String.format(
                "SELECT %s FROM %s WHERE %s",
                TableDefinitionUtil.fullColumnSerial(tableDefinition),
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.searchFragment(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)));
    }

    @Override
    protected String internalExistsSQL() {
        return String.format(
                "SELECT 1 FROM %s WHERE %s",
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.searchFragment(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)));
    }
}
