package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

import java.util.Objects;

/**
 * 适用于 FastJson 的 LongIdKey。
 *
 * <p>
 * 注意：该类中含有长整型的字段，在与JS前端通信中，会发生精度丢失的问题。
 * 请使用 {@link JSFixedFastJsonLongIdKey} 来解决JS前端的精度丢失问题。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonLongIdKey implements Key {

    private static final long serialVersionUID = 8863415673517584070L;

    /**
     * 根据指定的 LongIdKey 生成 FastJsonLongIdKey。
     *
     * @param longIdKey 指定的 LongIdKey。
     * @return 通过指定的 LongIdKey 生成的 FastJsonLongIdKey。
     */
    public static FastJsonLongIdKey of(LongIdKey longIdKey) {
        if (Objects.isNull(longIdKey)) {
            return null;
        }
        return new FastJsonLongIdKey(longIdKey.getLongId());
    }

    /**
     * 根据指定的 FastJsonLongIdKey 生成 LongIdKey。
     *
     * @param fastJsonLongIdKey 指定的 FastJsonLongIdKey。
     * @return 通过指定的 FastJsonLongIdKey 生成的 LongIdKey。
     * @since 1.2.13
     */
    public static LongIdKey toStackBean(FastJsonLongIdKey fastJsonLongIdKey) {
        if (Objects.isNull(fastJsonLongIdKey)) {
            return null;
        } else {
            return new LongIdKey(
                    fastJsonLongIdKey.getLongId()
            );
        }
    }

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
        return Long.hashCode(longId);
    }

    @Override
    public String toString() {
        return "FastJsonLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
