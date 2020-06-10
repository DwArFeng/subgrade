package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.td.PhoenixTDConstants;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinition.ConstraintDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.td.TableDefinitionUtil;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

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
                TableDefinitionUtil.searchFragment(findPrimaryKeyColumns()));
    }

    @Override
    protected String internalGetSQL() {
        return String.format(
                "SELECT %s FROM %s WHERE %s",
                TableDefinitionUtil.fullColumnSerial(tableDefinition),
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.searchFragment(findPrimaryKeyColumns()));
    }

    @Override
    protected String internalExistsSQL() {
        return String.format(
                "SELECT 1 FROM %s WHERE %s",
                TableDefinitionUtil.fullTableName(tableDefinition),
                TableDefinitionUtil.searchFragment(findPrimaryKeyColumns()));
    }

    private List<ColumnDefinition> findPrimaryKeyColumns() {
        ConstraintDefinition constraintDefinition = tableDefinition.getConstraintDefinitions().stream()
                .filter(def -> Objects.equals(def.getType(), PhoenixTDConstants.TYPE_PRIMARY_KEY)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("未能在数据表定义中找到任何主键，请确认该数据表定义中拥有" +
                        "类型为 " + PhoenixTDConstants.TYPE_PRIMARY_KEY + " 的约束"));
        if (Objects.isNull(constraintDefinition.getColumnDefinitions()) ||
                constraintDefinition.getColumnDefinitions().isEmpty()) {
            throw new IllegalArgumentException("数据表定义的主键约束中没有指定任何列");
        }
        return constraintDefinition.getColumnDefinitions();
    }
}
