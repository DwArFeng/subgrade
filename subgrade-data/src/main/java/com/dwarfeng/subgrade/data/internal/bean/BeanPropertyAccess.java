package com.dwarfeng.subgrade.data.internal.bean;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Bean 属性访问器。
 *
 * <p>
 * 该类集中提供数据访问实现需要的嵌套属性读取、属性写入和浅拷贝能力，避免生产代码依赖 Commons BeanUtils。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class BeanPropertyAccess {

    private static final BeanPropertyAccess INSTANCE = new BeanPropertyAccess();

    public static BeanPropertyAccess getInstance() {
        return INSTANCE;
    }

    /**
     * 获取 Bean 属性值。
     *
     * @param bean         指定的 Bean。
     * @param propertyPath 指定的属性路径。
     * @return 属性值。
     */
    public Object getProperty(Object bean, String propertyPath) {
        return new BeanWrapperImpl(bean).getPropertyValue(propertyPath);
    }

    /**
     * 设置 Bean 属性值。
     *
     * @param bean         指定的 Bean。
     * @param propertyPath 指定的属性路径。
     * @param value        指定的属性值。
     */
    public void setProperty(Object bean, String propertyPath, Object value) {
        new BeanWrapperImpl(bean).setPropertyValue(propertyPath, value);
    }

    /**
     * 浅拷贝 Bean。
     *
     * @param bean 指定的 Bean。
     * @param <T>  Bean 类型。
     * @return Bean 的浅拷贝。
     */
    @SuppressWarnings("unchecked")
    public <T> T cloneBean(T bean) {
        T clone = (T) BeanUtils.instantiateClass(bean.getClass());
        BeanUtils.copyProperties(bean, clone);
        return clone;
    }

    private BeanPropertyAccess() {
    }
}
