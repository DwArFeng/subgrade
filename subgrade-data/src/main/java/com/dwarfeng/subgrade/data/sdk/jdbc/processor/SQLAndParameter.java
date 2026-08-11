package com.dwarfeng.subgrade.data.sdk.jdbc.processor;

import com.dwarfeng.subgrade.basic.stack.bean.dto.Dto;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * SQL 语句与参数。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class SQLAndParameter implements Dto {

    @Serial
    private static final long serialVersionUID = 7404013281758188717L;

    private String sql;
    private List<Object[]> parametersList;

    public SQLAndParameter() {
    }

    /**
     * 生成只有 SQL 的新实例。
     *
     * <p>
     * 该方法会生成一个默认的参数组，参数组中含有 0 个元素。
     *
     * @param sql sql。
     * @since 1.2.0
     */
    public SQLAndParameter(String sql) {
        this.sql = sql;
        this.parametersList = Collections.singletonList(new Object[0]);
    }

    /**
     * 生成含有一个参数组的新实例。
     *
     * <p>
     * 参数 parameter 禁止为 <code>null</code>。
     *
     * @param sql       sql。
     * @param parameter parameter。
     * @since 1.2.0
     */
    public SQLAndParameter(@NotNull String sql, @NotNull Object[] parameter) {
        this.sql = sql;
        this.parametersList = Collections.singletonList(parameter);
    }

    /**
     * 生成含有多个参数组的新实例。
     *
     * <p>
     * 参数 parametersList 中不允许含有 <code>null</code> 元素。
     *
     * @param sql            sql。
     * @param parametersList parametersList。
     * @since 1.2.0
     */
    public SQLAndParameter(@NotNull String sql, @NotNull List<Object[]> parametersList) {
        this.sql = sql;
        this.parametersList = parametersList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(@NotNull String sql) {
        this.sql = sql;
    }

    public List<Object[]> getParametersList() {
        return parametersList;
    }

    /**
     * setParametersList。
     *
     * <p>
     * 参数 parametersList 中不允许含有 <code>null</code> 元素。
     *
     * @param parametersList parametersList。
     */
    public void setParametersList(@NotNull List<Object[]> parametersList) {
        this.parametersList = parametersList;
    }

    /**
     * 获取第一个参数组。
     *
     * @return 第一个参数组，如果没有，则返回一个长度为 0 的数组。
     * @since 1.2.0
     */
    public Object[] getFirstParameters() {
        if (this.parametersList.isEmpty()) {
            return null;
        } else {
            return this.parametersList.getFirst();
        }
    }

    @Override
    public String toString() {
        return "SQLAndParameter{" +
                "sql='" + sql + '\'' +
                ", parametersList=" + parametersList +
                '}';
    }
}
