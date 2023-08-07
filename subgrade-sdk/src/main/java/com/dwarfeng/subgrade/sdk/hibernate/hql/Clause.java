package com.dwarfeng.subgrade.sdk.hibernate.hql;

/**
 * 子句。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public interface Clause {

    /**
     * 将子句转换为 HQL。
     *
     * @return 子句对应的 HQL。
     */
    String toHql();
}
