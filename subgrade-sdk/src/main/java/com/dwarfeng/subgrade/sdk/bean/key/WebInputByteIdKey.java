package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 ByteIdKey。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class WebInputByteIdKey implements Key {

    private static final long serialVersionUID = 4151440822668418272L;

    /**
     * WebInputByteIdKey 转 ByteIdKey。
     *
     * @param webInputByteIdKey WebInputByteIdKey。
     * @return ByteIdKey。
     */
    public static ByteIdKey toStackBean(WebInputByteIdKey webInputByteIdKey) {
        if (Objects.isNull(webInputByteIdKey)) {
            return null;
        }
        return new ByteIdKey(webInputByteIdKey.getByteId());
    }

    @JSONField(name = "byte_id")
    private byte byteId;

    public WebInputByteIdKey() {
    }

    public WebInputByteIdKey(byte byteId) {
        this.byteId = byteId;
    }

    public byte getByteId() {
        return byteId;
    }

    public void setByteId(byte byteId) {
        this.byteId = byteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebInputByteIdKey that = (WebInputByteIdKey) o;

        return byteId == that.byteId;
    }

    @Override
    public int hashCode() {
        return byteId;
    }

    @Override
    public String toString() {
        return "WebInputByteIdKey{" +
                "byteId=" + byteId +
                '}';
    }
}
