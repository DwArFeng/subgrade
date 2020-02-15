package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 修正了 JS 精度问题的适用于 FastJson 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public class JSFixedFastJsonLongIdKey implements Bean {

    private static final long serialVersionUID = 8863415673517584070L;

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
        return (int) (longId ^ (longId >>> 32));
    }

    @Override
    public String toString() {
        return "FastJsonLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
