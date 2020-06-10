package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.database.PhoenixUtil;
import com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinitionUtil;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

import javax.validation.constraints.NotNull;

/**
 * 通用预设查询模板。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class PhoenixPresetLookupTemplate extends GeneralPresetLookupTemplate {

    private static final String CACHE_LOOKUP = "lookup";
    private static final String CACHE_PAGING = "paging";
    private static final String CACHE_COUNT = "count";

    public PhoenixPresetLookupTemplate(@NotNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    public String lookupSQL(String preset, Object[] args) {
        String fragmentSQL;
        if (containsCachedSQLFragment(CACHE_LOOKUP)) {
            fragmentSQL = getCachedSQLFragment(CACHE_LOOKUP);
        } else {
            fragmentSQL = String.format("SELECT %s FROM %s WHERE %%s",
                    TableDefinitionUtil.fullColumnSerial(tableDefinition),
                    TableDefinitionUtil.fullTableName(tableDefinition));
            putCachedSQLFragment(CACHE_LOOKUP, fragmentSQL);
        }
        return String.format(fragmentSQL, buildWhereClause(preset, args));
    }

    @Override
    public String pagingSQL(String preset, Object[] args, PagingInfo pagingInfo) {
        String fragmentSQL;
        if (containsCachedSQLFragment(CACHE_PAGING)) {
            fragmentSQL = getCachedSQLFragment(CACHE_PAGING);
        } else {
            fragmentSQL = String.format("SELECT %s FROM %s WHERE %%s OFFSET %%d LIMIT %%d",
                    TableDefinitionUtil.fullColumnSerial(tableDefinition),
                    TableDefinitionUtil.fullTableName(tableDefinition));
            putCachedSQLFragment(CACHE_PAGING, fragmentSQL);
        }
        return String.format(fragmentSQL, buildWhereClause(preset, args),
                Math.max(pagingInfo.getPage() * pagingInfo.getRows(), 0), Math.max(pagingInfo.getRows(), 0));
    }

    @Override
    public String countSQL(String preset, Object[] args) {
        String fragmentSQL;
        if (containsCachedSQLFragment(CACHE_COUNT)) {
            fragmentSQL = getCachedSQLFragment(CACHE_COUNT);
        } else {
            fragmentSQL = String.format("SELECT COUNT(%s) FROM %s WHERE %%s",
                    TableDefinitionUtil.columnSerial(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)),
                    TableDefinitionUtil.fullTableName(tableDefinition));
            putCachedSQLFragment(CACHE_COUNT, fragmentSQL);
        }
        return String.format(fragmentSQL, buildWhereClause(preset, args));
    }

    /**
     * 构造查询子句。
     *
     * <p>
     * 例如：
     * <code>SELECT id, name, age FROM t WHERE <u>name LIKE 'd%' and age > 18<u/></code>
     *
     * @param preset 查询模板。
     * @param args   查询参数。
     * @return 查询子句。
     */
    public abstract String buildWhereClause(String preset, Object[] args);
}
