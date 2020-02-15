package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 适用于 FastJson 的 LongIdKey。
 * <p>注意：该类中含有长整型的字段，在与JS前端通信中，会发生精度丢失的问题。
 * 请使用 {@link JSFixedFastJsonLongIdKey} 来解决JS前端的工薪问题</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonLongIdKey implements Bean {

    private static final long serialVersionUID = 8863415673517584070L;

    @JSONField(name = "long_id", ordinal = 1)
    private long longId;

    public FastJsonLongIdKey() {
    }

    public FastJsonLongIdKey(long id) {
        this.longId = id;
    }

    public long getLongId() {
        return longId;
    }

    public void setLongId(long longId) {
        this.longId = longId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastJsonLongIdKey that = (FastJsonLongIdKey) o;

        return longId == that.longId;
    }

    @Override
    public int hashCode() {
        return (int) (longId ^ (longId >>> 32));
    }

    @Override
    public String toString() {
        return "FastJsonLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
