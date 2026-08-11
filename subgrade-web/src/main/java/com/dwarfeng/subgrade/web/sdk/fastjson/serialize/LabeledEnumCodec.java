package com.dwarfeng.subgrade.web.sdk.fastjson.serialize;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.dwarfeng.subgrade.basic.sdk.enumeration.LabeledEnum;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * LabeledEnum Fastjson2 编解码器。
 *
 * <p>
 * 该编解码器使用枚举标签作为 JSON 文本值。具体枚举应通过无参数子类向 Fastjson2 注解提供确定的运行时类型。
 *
 * @param <E> 枚举类型。
 * @author DwArFeng
 * @since 2.0.0
 */
public abstract class LabeledEnumCodec<E extends Enum<E> & LabeledEnum>
        implements ObjectWriter<E>, ObjectReader<E> {

    private final Class<? extends E> clazz;

    public LabeledEnumCodec(Class<? extends E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (Objects.isNull(object)) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeString(clazz.cast(object).getLabel());
    }

    @Override
    public long getFeatures() {
        return 0L;
    }

    @Override
    public E readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String label = jsonReader.readString();
        if (Objects.isNull(label)) {
            return null;
        }
        for (E enumConstant : clazz.getEnumConstants()) {
            if (Objects.equals(label, enumConstant.getLabel())) {
                return enumConstant;
            }
        }
        throw new IllegalStateException("枚举类 " + clazz.getCanonicalName() + " 中找不到标签为 " + label + " 的值");
    }

    public Class<? extends E> getClazz() {
        return clazz;
    }
}
