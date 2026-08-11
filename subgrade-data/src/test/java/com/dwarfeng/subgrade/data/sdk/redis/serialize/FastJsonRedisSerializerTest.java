package com.dwarfeng.subgrade.data.sdk.redis.serialize;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * FastJson Redis 序列化器测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class FastJsonRedisSerializerTest {

    @Test
    public void shouldRestoreDeclaredRuntimeTypeWithoutAcceptedTypes() {
        FastJsonRedisSerializer<TestBean> serializer = new FastJsonRedisSerializer<>(TestBean.class);
        TestBean source = new TestBean("value");

        TestBean target = serializer.deserialize(serializer.serialize(source));

        assertEquals(source.getValue(), target.getValue());
    }

    @Test
    public void shouldRestoreAcceptedRuntimeTypeWhenDeclaredTypeIsObject() {
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class, TestBean.class);
        TestBean source = new TestBean("value");

        Object target = serializer.deserialize(serializer.serialize(source));

        TestBean testBean = assertInstanceOf(TestBean.class, target);
        assertEquals(source.getValue(), testBean.getValue());
    }

    @Test
    public void shouldFallbackToJsonObjectWhenRuntimeTypeIsUnaccepted() {
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        TestBean source = new TestBean("value");

        Object target = serializer.deserialize(serializer.serialize(source));

        JSONObject jsonObject = assertInstanceOf(JSONObject.class, target);
        assertEquals(source.getValue(), jsonObject.getString("value"));
    }

    public static class TestBean {

        private String value;

        public TestBean() {
        }

        public TestBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
