package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixUtil;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinitionUtil;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    private static final String CACHE_SELECT_FROM = "select_from";
    private static final String CACHE_SELECT_COUNT_FROM = "select_count_from";

    public PhoenixPresetLookupTemplate(@NotNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    public String lookupSQL(String preset, Object[] args) {
        StringBuilder sqlBuilder = new StringBuilder();
        appendSelectFrom(sqlBuilder);
        String whereClause = buildWhereClause(preset, args);
        String orderByClause = buildOrderByClause(preset, args);
        if (Objects.nonNull(whereClause) && !whereClause.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(whereClause);
        }
        if (Objects.nonNull(orderByClause) && !orderByClause.isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(orderByClause);
        }
        return sqlBuilder.toString();
    }

    @Override
    public String pagingSQL(String preset, Object[] args, PagingInfo pagingInfo) {
        StringBuilder sqlBuilder = new StringBuilder();
        appendSelectFrom(sqlBuilder);
        String whereClause = buildWhereClause(preset, args);
        String orderByClause = buildOrderByClause(preset, args);
        if (Objects.nonNull(whereClause) && !whereClause.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(whereClause);
        }
        if (Objects.nonNull(orderByClause) && !orderByClause.isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(orderByClause);
        }
        sqlBuilder.append(" OFFSET ").append(Math.max(pagingInfo.getPage() * pagingInfo.getRows(), 0))
                .append(" LIMIT ").append(Math.max(pagingInfo.getRows(), 0));
        return sqlBuilder.toString();
    }

    @Override
    public String countSQL(String preset, Object[] args) {
        StringBuilder sqlBuilder = new StringBuilder();
        appendSelectCountFrom(sqlBuilder);
        String whereClause = buildWhereClause(preset, args);
        String orderByClause = buildOrderByClause(preset, args);
        if (Objects.nonNull(whereClause) && !whereClause.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(whereClause);
        }
        if (Objects.nonNull(orderByClause) && !orderByClause.isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(orderByClause);
        }
        return sqlBuilder.toString();
    }

    /**
     * 构造查询子句。
     *
     * <p>
     * 例如：
     * <code>SELECT id, name, age FROM t WHERE <u>name LIKE 'd%' and age > 18</u> ORDER BY age ASC</code>
     *
     * <p>
     * 如果指定的参数没有对应的查询子句，可以直接返回 null。
     *
     * @param preset 查询模板。
     * @param args   查询参数。
     * @return 查询子句。
     */
    protected abstract String buildWhereClause(String preset, Object[] args);

    /**
     * 构造排序子句。
     *
     * <p>
     * 例如：
     * <code>SELECT id, name, age FROM t WHERE name LIKE 'd%' and age > 18 ORDER BY <u>age ASC<u/></code>
     *
     * <p>
     * 如果指定的参数没有对应的排序子句，可以直接返回 null。
     *
     * @param preset 查询模板。
     * @param args   查询参数。
     * @return 排序子句。
     */
    protected abstract String buildOrderByClause(String preset, Object[] args);

    private void appendSelectFrom(StringBuilder sqlBuilder) {
        if (containsCachedSQLFragment(CACHE_SELECT_FROM)) {
            sqlBuilder.append(getCachedSQLFragment(CACHE_SELECT_FROM));
        } else {
            String selectFromFragment = String.format("SELECT %s FROM %s",
                    TableDefinitionUtil.fullColumnSerial(tableDefinition),
                    TableDefinitionUtil.fullTableName(tableDefinition));
            putCachedSQLFragment(CACHE_SELECT_FROM, selectFromFragment);
            sqlBuilder.append(selectFromFragment);
        }
    }

    private void appendSelectCountFrom(StringBuilder sqlBuilder) {
        if (containsCachedSQLFragment(CACHE_SELECT_COUNT_FROM)) {
            sqlBuilder.append(getCachedSQLFragment(CACHE_SELECT_COUNT_FROM));
        } else {
            String selectFromFragment = String.format("SELECT COUNT(%s) FROM %s",
                    TableDefinitionUtil.columnSerial(PhoenixUtil.findPrimaryKeyColumns(tableDefinition)),
                    TableDefinitionUtil.fullTableName(tableDefinition));
            putCachedSQLFragment(CACHE_SELECT_COUNT_FROM, selectFromFragment);
            sqlBuilder.append(selectFromFragment);
        }
    }
}
