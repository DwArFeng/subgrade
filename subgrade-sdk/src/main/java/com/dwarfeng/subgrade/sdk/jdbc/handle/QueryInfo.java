package com.dwarfeng.subgrade.sdk.jdbc.handle;

import com.dwarfeng.subgrade.stack.bean.dto.Dto;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

/**
 * 查询信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class QueryInfo implements Dto {

    private static final long serialVersionUID = -4356783932649190318L;

    private String whereClause;
    private Map<String, Ordering> orderingMap;
    private Object[] parameters;

    public QueryInfo() {
    }

    public QueryInfo(
            @Nonnull String whereClause, @Nonnull Map<String, Ordering> orderingMap, @Nonnull Object[] parameters) {
        this.whereClause = whereClause;
        this.orderingMap = orderingMap;
        this.parameters = parameters;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(@Nonnull String whereClause) {
        this.whereClause = whereClause;
    }

    public Map<String, Ordering> getOrderingMap() {
        return orderingMap;
    }

    public void setOrderingMap(@Nonnull Map<String, Ordering> orderingMap) {
        this.orderingMap = orderingMap;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(@Nonnull Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "HqlQueryInfo{" +
                "whereClause='" + whereClause + '\'' +
                ", orderingMap=" + orderingMap +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }

    /**
     * 排序类型。
     *
     * @author DwArFeng
     * @since 1.2.0
     */
    public enum Ordering {
        ASC, DESC
    }
}
