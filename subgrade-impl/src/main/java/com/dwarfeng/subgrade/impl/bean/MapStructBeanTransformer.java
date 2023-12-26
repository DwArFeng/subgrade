package com.dwarfeng.subgrade.impl.bean;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 使用 MapStruct 实现的 Bean 映射器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public class MapStructBeanTransformer<U extends Bean, V extends Bean> implements BeanTransformer<U, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapStructBeanTransformer.class);

    private final Class<U> classU;
    private final Class<V> classV;
    private final Class<?> mapperClass;

    private Object mapper;
    private Method transformMethod;
    private Method reverseTransformMethod;

    /**
     * 新实例。
     *
     * @param classU      对象 U 的类。
     * @param classV      对象 V 的类。
     * @param mapperClass Mapper 的类。
     */
    public MapStructBeanTransformer(
            @Nonnull Class<U> classU, @Nonnull Class<V> classV, @Nonnull Class<?> mapperClass
    ) {
        this.classU = classU;
        this.classV = classV;
        this.mapperClass = mapperClass;
        init();
    }

    private void init() {
        // 获取映射器。
        mapper = Mappers.getMapper(mapperClass);

        // 遍历映射器方法，获取入参类型/返回类型符合 classU 和 classV 的方法，
        // 或者是入参类型/返回类型符合 classV 和 classU 的方法。
        for (Method method : mapperClass.getMethods()) {
            if (Objects.nonNull(transformMethod) && Objects.nonNull(reverseTransformMethod)) {
                break;
            }
            if (Objects.isNull(transformMethod)) {
                checkTransformMethod(method);
            }
            if (Objects.isNull(reverseTransformMethod)) {
                checkReverseTransformMethod(method);
            }
        }

        if (Objects.isNull(transformMethod) || Objects.isNull(reverseTransformMethod)) {
            String message = String.format(
                    "找不到合适的方法, 请确认 Mapper 中存在参类型/返回类型为 %1$s 和 %2$s 的方法, " +
                            "以及参类型/返回类型为 %2$s 和 %1$s 的方法",
                    classU.getCanonicalName(), classV.getCanonicalName()
            );
            throw new IllegalStateException(message);
        }
    }

    private void checkTransformMethod(Method method) {
        if (!(method.getParameterCount() == 1)) {
            return;
        }

        Class<?> parameterClass = method.getParameterTypes()[0];
        Class<?> returnClass = method.getReturnType();

        if (classU.isAssignableFrom(parameterClass) && classV.isAssignableFrom(returnClass)) {
            // 日志输出。
            LOGGER.debug(
                    "找到适配的变换方法, 方法名: {}, 入参类型: {}, 返回值类型: {}",
                    method.getName(), parameterClass.getCanonicalName(), returnClass.getCanonicalName()
            );

            transformMethod = method;
        }
    }

    private void checkReverseTransformMethod(Method method) {
        if (!(method.getParameterCount() == 1)) {
            return;
        }

        Class<?> parameterClass = method.getParameterTypes()[0];
        Class<?> returnClass = method.getReturnType();

        if (classV.isAssignableFrom(parameterClass) && classU.isAssignableFrom(returnClass)) {
            // 日志输出。
            LOGGER.debug(
                    "找到适配的逆变换方法, 方法名: {}, 入参类型: {}, 返回值类型: {}",
                    method.getName(), parameterClass.getCanonicalName(), returnClass.getCanonicalName()
            );

            reverseTransformMethod = method;
        }
    }

    @Override
    public V transform(U u) {
        try {
            return classV.cast(transformMethod.invoke(mapper, u));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public U reverseTransform(V v) {
        try {
            return classU.cast(reverseTransformMethod.invoke(mapper, v));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
