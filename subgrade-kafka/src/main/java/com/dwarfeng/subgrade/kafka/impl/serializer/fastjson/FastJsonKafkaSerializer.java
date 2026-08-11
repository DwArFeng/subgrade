package com.dwarfeng.subgrade.kafka.impl.serializer.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.kafka.common.serialization.Serializer;

/**
 * 使用 Fastjson2 进行序列化的 Kafka 序列化器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class FastJsonKafkaSerializer<T> implements Serializer<T> {

    public FastJsonKafkaSerializer() {
        super();
    }

    @Override
    public byte[] serialize(String topic, T value) {
        if (value == null) {
            return null;
        }
        return JSON.toJSONBytes(value, JSONWriter.Feature.WriteClassName);
    }
}
