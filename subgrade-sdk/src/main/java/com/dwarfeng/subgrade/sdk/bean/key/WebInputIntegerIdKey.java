package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.IntegerIdKey;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 IntegerIdKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputIntegerIdKey implements Key {

    private static final long serialVersionUID = -5919040604668758118L;

    /**
     * WebInputIntegerIdKey 转 IntegerIdKey。
     *
     * @param webInputIntegerIdKey WebInputIntegerIdKey。
     * @return IntegerIdKey。
     */
    public static IntegerIdKey toStackBean(WebInputIntegerIdKey webInputIntegerIdKey) {
        if (Objects.isNull(webInputIntegerIdKey)) {
            return null;
        }
        return new IntegerIdKey(webInputIntegerIdKey.getIntegerId());
    }

    @JSONField(name = "integer_id")
    private int integerId;

    public WebInputIntegerIdKey() {
    }

    public WebInputIntegerIdKey(int integerId) {
        this.integerId = integerId;
    }

    public int getIntegerId() {
        return integerId;
    }

    public void setIntegerId(int integerId) {
        this.integerId = integerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebInputIntegerIdKey that = (WebInputIntegerIdKey) o;

        return integerId == that.integerId;
    }

    @Override
    public int hashCode() {
        return integerId;
    }

    @Override
    public String toString() {
        return "WebInputIntegerIdKey{" +
                "integerId=" + integerId +
                '}';
    }
}
