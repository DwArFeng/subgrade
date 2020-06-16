package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixUtil;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinitionUtil;
import org.springframework.lang.NonNull;

/**
 * Phoenix 整体查询模板。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixEntireLookupTemplate extends GeneralEntireLookupTemplate {

    public PhoenixEntireLookupTemplate(@NonNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    protected String internalLookupSQL() {
        return String.format(
                "SELECT %s FROM %s",
                TableDefinitionUtil.fullColumnSerial(tableDefinition),
                TableDefinitionUtil.fullTableName(tableDefinition));
    }

    @Override
    protected String internalPagingSQL() {
        return String.format(
                "SELECT %s FROM %s OFFSET ? LIMIT ?",
                TableDefinitionUtil.fullColumnSerial(tableDefinition),
                TableDefinitionUtil.fullTableName(tableDefinition));
    }

    @Override
    protected String internalCountSQL() {
        return String.format(
                "SELECT COUNT(%s) FROM %s",
                TableDefinitionUtil.columnSerial(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)),
                TableDefinitionUtil.fullTableName(tableDefinition));
    }
}
