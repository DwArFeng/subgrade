package com.dwarfeng.subgrade.sdk.jdbc;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;

import java.util.Arrays;
import java.util.List;

/**
 * SQL语句与参数。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class SQLAndParameter implements Dto {

    private static final long serialVersionUID = -794551499863792624L;

    private String sql;
    private Object[] parameters;
    private List<Object[]> parametersList;

    public SQLAndParameter() {
    }

    public SQLAndParameter(String sql, Object[] parameters, List<Object[]> parametersList) {
        this.sql = sql;
        this.parameters = parameters;
        this.parametersList = parametersList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public List<Object[]> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<Object[]> parametersList) {
        this.parametersList = parametersList;
    }

    @Override
    public String toString() {
        return "SQLAndParameter{" +
                "sql='" + sql + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                ", parametersList=" + parametersList +
                '}';
    }
}
