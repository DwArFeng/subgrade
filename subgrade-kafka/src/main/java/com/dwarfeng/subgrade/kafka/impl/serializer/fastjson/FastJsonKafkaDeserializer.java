package com.dwarfeng.subgrade.kafka.impl.serializer.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.dwarfeng.subgrade.kafka.internal.i18n.KafkaMessageKey;
import com.dwarfeng.subgrade.kafka.internal.i18n.KafkaMessages;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

/**
 * 使用 Fastjson2 进行反序列化的 Kafka 反序列化器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class FastJsonKafkaDeserializer<T> implements Deserializer<T> {

    private Class<T> clazz;
    private final JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;

    public FastJsonKafkaDeserializer(Class<T> clazz) {
        this(clazz, new Class<?>[0]);
    }

    /**
     * 构造 FastJson Kafka 反序列化器。
     *
     * <p>
     * 当声明类型是接口、抽象类或 {@link Object} 时，
     * 需要通过 <code>acceptedTypes</code> 显式声明允许从 <code>@type</code> 恢复的运行时类型。
     * 未声明的运行时类型不会被加载。
     *
     * @param clazz         声明类型。
     * @param acceptedTypes 允许反序列化的运行时类型。
     */
    public FastJsonKafkaDeserializer(Class<T> clazz, Class<?>... acceptedTypes) {
        this.clazz = clazz;
        this.autoTypeBeforeHandler = JSONReader.autoTypeFilter(acceptedTypes);
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        try {
            return JSON.parseObject(str, clazz, autoTypeBeforeHandler);
        } catch (JSONException e) {
            throw new SerializationException(
                    KafkaMessages.message(KafkaMessageKey.FAST_JSON_DESERIALIZATION_FAILED), e
            );
        }
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
