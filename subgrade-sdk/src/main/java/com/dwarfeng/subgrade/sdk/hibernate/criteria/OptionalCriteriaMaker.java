package com.dwarfeng.subgrade.sdk.hibernate.criteria;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * 可选 Criterion 制造器。
 *
 * <p>
 * 提供一个支持的 preset 名称，以及根据指定的对象构造 Criterion。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface OptionalCriteriaMaker {

    /**
     * 根据指定的预设和参数构造 Criterion。
     *
     * @param preset 指定的预设。
     * @param objs   指定的参数。
     * @return 指定的参数构造的 Criterion。
     */
    Criterion makeCriterion(String preset, Object[] objs);

    /**
     * 根据指定的预设和参数构造 Order。
     *
     * @param preset 指定的预设。
     * @param objs   指定的参数。
     * @return 指定的参数构造的 Order。
     */
    Order makeOrder(String preset, Object[] objs);
}
