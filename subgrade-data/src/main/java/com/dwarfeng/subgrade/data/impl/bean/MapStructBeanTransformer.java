package com.dwarfeng.subgrade.data.impl.bean;

import org.jetbrains.annotations.NotNull;

/**
 * 使用 MapStruct 实现的 Bean 映射器。
 *
 * @author DwArFeng
 * @since 1.3.0
 * @deprecated 请使用 {@link com.dwarfeng.subgrade.basic.impl.bean.MapStructBeanTransformer}。
 */
@Deprecated
public class MapStructBeanTransformer<U extends com.dwarfeng.subgrade.basic.stack.bean.Bean,
        V extends com.dwarfeng.subgrade.basic.stack.bean.Bean>
        extends com.dwarfeng.subgrade.basic.impl.bean.MapStructBeanTransformer<U, V> {

    /**
     * 新实例。
     *
     * @param classU      对象 U 的类。
     * @param classV      对象 V 的类。
     * @param mapperClass Mapper 的类。
     */
    public MapStructBeanTransformer(
            @NotNull Class<U> classU, @NotNull Class<V> classV, @NotNull Class<?> mapperClass
    ) {
        super(classU, classV, mapperClass);
    }
}
