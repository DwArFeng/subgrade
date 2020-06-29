package com.dwarfeng.subgrade.sdk.database.jdbc;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;

import java.util.Arrays;
import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public class QueryInfo implements Dto {

    private static final long serialVersionUID = -2124848214811463945L;

    private String whereClause;
    private List<String> orders;
    private Object[] parameters;

    public QueryInfo() {
    }

    public QueryInfo(String whereClause, List<String> orders, Object[] parameters) {
        this.whereClause = whereClause;
        this.orders = orders;
        this.parameters = parameters;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "QueryInfo{" +
                "whereClause='" + whereClause + '\'' +
                ", orders=" + orders +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
