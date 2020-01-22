package com.dwarfeng.subgrade.impl.bean;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import org.dozer.Mapper;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 使用 Dozer 实现的 Bean 映射器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class DozerBeanTransformer<U extends Bean, V extends Bean> implements BeanTransformer<U, V> {

    private Class<U> classU;
    private Class<V> classV;
    private Mapper mapper;

    public DozerBeanTransformer(
            @NonNull Class<U> classU,
            @NonNull Class<V> classV,
            @NonNull Mapper mapper) {
        this.classU = classU;
        this.classV = classV;
        this.mapper = mapper;
    }

    @Override
    public V transform(U u) {
        if (Objects.isNull(u)) {
            return null;
        }
        return mapper.map(u, classV);
    }

    @Override
    public U reverseTransform(V v) {
        if (Objects.isNull(v)) {
            return null;
        }
        return mapper.map(v, classU);
    }

    public Class<U> getClazzU() {
        return classU;
    }

    public void setClazzU(@NonNull Class<U> classU) {
        this.classU = classU;
    }

    public Class<V> getClazzV() {
        return classV;
    }

    public void setClazzV(@NonNull Class<V> classV) {
        this.classV = classV;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(@NonNull Mapper mapper) {
        this.mapper = mapper;
    }
}
