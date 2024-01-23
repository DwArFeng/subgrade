package com.dwarfeng.subgrade.sdk.hibernate.criteria;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

/**
 * 无排序的 DetachedCriteria。
 *
 * <p>
 * 该类的 {@link #addOrder(Order)} 方法不会添加任何排序，可以用于 count 等不需要排序的查询。
 *
 * @author DwArFeng
 * @since 1.4.8
 */
public class NoOrderDetachedCriteria extends DetachedCriteria {

    @SuppressWarnings("rawtypes")
    public static NoOrderDetachedCriteria forClass(Class clazz) {
        return new NoOrderDetachedCriteria(clazz.getName());
    }

    protected NoOrderDetachedCriteria(String entityName) {
        super(entityName);
    }

    @Override
    public DetachedCriteria addOrder(Order order) {
        // Do nothing.
        return this;
    }

    @Override
    public String toString() {
        return "NoOrderDetachedCriteria{}";
    }
}
