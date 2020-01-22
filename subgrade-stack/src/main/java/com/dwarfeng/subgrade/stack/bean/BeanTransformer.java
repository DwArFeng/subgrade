package com.dwarfeng.subgrade.stack.bean;

/**
 * Bean映射器。
 * <p>映射两个Bean对象，使其能够相互转化。</p>
 *
 * @param <U> 对象 U。
 * @param <V> 对象 V。
 */
public interface BeanTransformer<U extends Bean, V extends Bean> {

    /**
     * 变换-将 U 变成 V。
     *
     * @param u 对象 U。
     * @return 由 U 变换而成的对象 V。
     */
    V transform(U u);

    /**
     * 逆变换-将 V 变成 U。
     *
     * @param v 对象 V。
     * @return 由 V 变换而成的对象 U。
     */
    U reverseTransform(V v);
}
