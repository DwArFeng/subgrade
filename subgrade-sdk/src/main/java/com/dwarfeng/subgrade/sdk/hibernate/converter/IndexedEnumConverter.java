package com.dwarfeng.subgrade.sdk.hibernate.converter;

import com.dwarfeng.subgrade.sdk.enumeration.IndexedEnum;

import javax.persistence.AttributeConverter;
import java.util.Objects;

/**
 * IndexedEnum 转换器。
 *
 * <p>
 * 注意，该类是一个抽象类，其子类必须实现无参数构造器方法。
 *
 * @author DwArFeng
 * @since 1.1.5
 */
public abstract class IndexedEnumConverter<E extends Enum<E> & IndexedEnum> implements AttributeConverter<E, Integer> {

    private final Class<? extends E> clazz;

    public IndexedEnumConverter(Class<? extends E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        if (Objects.isNull(attribute)) return null;
        return attribute.getIndex();
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        if (Objects.isNull(dbData)) return null;
        for (E enumConstant : clazz.getEnumConstants()) {
            if (Objects.equals(dbData, enumConstant.getIndex())) {
                return enumConstant;
            }
        }
        throw new IllegalStateException("枚举类 " + clazz.getCanonicalName() + " 中找不到索引为 " + dbData + " 的值");
    }
}
