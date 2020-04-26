package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.ByteIdKey;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.Objects;

/**
 * 适用于 FastJson 的 ByteIdKey。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class FastJsonByteIdKey implements Key {

    private static final long serialVersionUID = -680280775229538765L;

    /**
     * 根据指定的 ByteIdKey 生成 FastJsonByteIdKey。
     *
     * @param byteIdKey 指定的 ByteIdKey。
     * @return 通过指定的 ByteIdKey 生成的 FastJsonByteIdKey。
     */
    public static FastJsonByteIdKey of(ByteIdKey byteIdKey) {
        if (Objects.isNull(byteIdKey)) {
            return null;
        }
        return new FastJsonByteIdKey(byteIdKey.getByteId());
    }

    @JSONField(name = "byte_id", ordinal = 1)
    private byte byteId;

    public FastJsonByteIdKey() {
    }

    public FastJsonByteIdKey(byte byteId) {
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

        FastJsonByteIdKey that = (FastJsonByteIdKey) o;

        return byteId == that.byteId;
    }

    @Override
    public int hashCode() {
        return byteId;
    }

    @Override
    public String toString() {
        return "FastJsonByteIdKey{" +
                "byteId=" + byteId +
                '}';
    }
}
