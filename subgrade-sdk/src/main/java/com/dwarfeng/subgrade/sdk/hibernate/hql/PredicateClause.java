package com.dwarfeng.subgrade.sdk.hibernate.hql;

import java.util.Map;

/**
 * 谓词子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public interface PredicateClause extends Clause {

    /**
     * 将子句转换为参数映射。
     *
     * <p>
     * 返回的参数映射中的键不用担心与其它子句的键重复，只需保证自身子句中的键不重复即可。
     *
     * @return 子句对应的参数映射。
     */
    Map<String, Object> toParamMap();
}
