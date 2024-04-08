package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

import java.util.Objects;

/**
 * 修正了 JS 精度问题的适用于 FastJson 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public class JSFixedFastJsonLongIdKey implements Bean {

    private static final long serialVersionUID = 8863415673517584070L;

    /**
     * 根据指定的 LongIdKey 生成 JSFixedFastJsonLongIdKey。
     *
     * @param longIdKey 指定的 LongIdKey。
     * @return 通过指定的 LongIdKey 生成的 JSFixedFastJsonLongIdKey。
     */
    public static JSFixedFastJsonLongIdKey of(LongIdKey longIdKey) {
        if (Objects.isNull(longIdKey)) {
            return null;
        }
        return new JSFixedFastJsonLongIdKey(longIdKey.getLongId());
    }

    /**
     * 根据指定的 JSFixedFastJsonLongIdKey 生成 LongIdKey。
     *
     * @param fastJsonLongIdKey 指定的 JSFixedFastJsonLongIdKey。
     * @return 通过指定的 JSFixedFastJsonLongIdKey 生成的 LongIdKey。
     * @since 1.2.13
     */
    public static JSFixedFastJsonLongIdKey toStackBean(JSFixedFastJsonLongIdKey fastJsonLongIdKey) {
        if (Objects.isNull(fastJsonLongIdKey)) {
            return null;
        } else {
            return new JSFixedFastJsonLongIdKey(
                    fastJsonLongIdKey.getLongId()
            );
        }
    }

    @JSONField(name = "long_id", ordinal = 1, serializeUsing = ToStringSerializer.class)
    private long longId;

    public JSFixedFastJsonLongIdKey() {
    }

    public JSFixedFastJsonLongIdKey(long id) {
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

        JSFixedFastJsonLongIdKey that = (JSFixedFastJsonLongIdKey) o;

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
