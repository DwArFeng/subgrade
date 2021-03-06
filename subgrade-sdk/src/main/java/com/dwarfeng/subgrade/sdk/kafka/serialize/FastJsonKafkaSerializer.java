package com.dwarfeng.subgrade.sdk.kafka.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.kafka.common.serialization.Serializer;

/**
 * 使用FastJson进行序列化的Redis序列化器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class FastJsonKafkaSerializer<T> implements Serializer<T> {

    public FastJsonKafkaSerializer() {
        super();
    }

    @Override
    public byte[] serialize(String topic, T t) {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes();
    }
}
