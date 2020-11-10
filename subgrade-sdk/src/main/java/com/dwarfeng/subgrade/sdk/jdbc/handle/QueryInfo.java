package com.dwarfeng.subgrade.sdk.jdbc.handle;

import java.util.Arrays;
import java.util.Map;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public class QueryInfo {

    private final String whereClause;
    private final Map<String, Ordering> orderingMap;
    private final Object[] parameters;

    public QueryInfo(String whereClause, Map<String, Ordering> orderingMap, Object[] parameters) {
        this.whereClause = whereClause;
        this.orderingMap = orderingMap;
        this.parameters = parameters;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public Map<String, Ordering> getOrderingMap() {
        return orderingMap;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "QueryInfo{" +
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
