package com.dwarfeng.subgrade.data.sdk.jdbc.handle;

import com.dwarfeng.subgrade.basic.stack.bean.dto.Dto;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;

/**
 * 查询信息。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class QueryInfo implements Dto {

    @Serial
    private static final long serialVersionUID = 8613620720044859395L;

    private String whereClause;
    private Map<String, Ordering> orderingMap;
    private Object[] parameters;

    public QueryInfo() {
    }

    public QueryInfo(
            @NotNull String whereClause, @NotNull Map<String, Ordering> orderingMap, @NotNull Object[] parameters) {
        this.whereClause = whereClause;
        this.orderingMap = orderingMap;
        this.parameters = parameters;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(@NotNull String whereClause) {
        this.whereClause = whereClause;
    }

    public Map<String, Ordering> getOrderingMap() {
        return orderingMap;
    }

    public void setOrderingMap(@NotNull Map<String, Ordering> orderingMap) {
        this.orderingMap = orderingMap;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(@NotNull Object[] parameters) {
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
