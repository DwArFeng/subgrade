package com.dwarfeng.subgrade.data.sdk.redis.serialize;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessageKey;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessages;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 使用 FastJson 进行序列化的 Redis 序列化器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> clazz;
    private final JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;

    public FastJsonRedisSerializer(Class<T> clazz) {
        this(clazz, new Class<?>[0]);
    }

    /**
     * 构造 FastJson Redis 序列化器。
     *
     * <p>
     * 当声明类型是接口、抽象类或 {@link Object} 时，
     * 需要通过 <code>acceptedTypes</code> 显式声明允许从 <code>@type</code> 恢复的运行时类型。
     * 未声明的运行时类型不会被加载。
     *
     * @param clazz         声明类型。
     * @param acceptedTypes 允许反序列化的运行时类型。
     */
    public FastJsonRedisSerializer(Class<T> clazz, Class<?>... acceptedTypes) {
        super();
        this.clazz = clazz;
        this.autoTypeBeforeHandler = JSONReader.autoTypeFilter(acceptedTypes);
    }

    @NullMarked
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(bytes, clazz, autoTypeBeforeHandler);
        } catch (JSONException e) {
            throw new SerializationException(
                    DataMessages.message(DataMessageKey.FAST_JSON_DESERIALIZATION_FAILED), e
            );
        }
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "FastJsonRedisSerializer{" +
                "clazz=" + clazz +
                '}';
    }
}
