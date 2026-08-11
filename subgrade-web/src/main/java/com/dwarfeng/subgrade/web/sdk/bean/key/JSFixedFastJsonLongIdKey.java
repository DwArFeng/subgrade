package com.dwarfeng.subgrade.web.sdk.bean.key;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.writer.ObjectWriterImplToString;
import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.key.LongIdKey;

import java.io.Serial;
import java.util.Objects;

/**
 * 修正了 JS 精度问题的适用于 FastJson 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.2.1-beta
 */
public class JSFixedFastJsonLongIdKey implements Bean {

    @Serial
    private static final long serialVersionUID = 5705339812584667040L;
    @JSONField(name = "long_id", ordinal = 1, serializeUsing = ObjectWriterImplToString.class)
    private long longId;

    public JSFixedFastJsonLongIdKey() {
    }

    public JSFixedFastJsonLongIdKey(long id) {
        this.longId = id;
    }

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
     * @param jsFixedFastJsonLongIdKey 指定的 JSFixedFastJsonLongIdKey。
     * @return 通过指定的 JSFixedFastJsonLongIdKey 生成的 LongIdKey。
     * @since 1.2.13
     */
    public static LongIdKey toStackBean(JSFixedFastJsonLongIdKey jsFixedFastJsonLongIdKey) {
        if (Objects.isNull(jsFixedFastJsonLongIdKey)) {
            return null;
        } else {
            return new LongIdKey(
                    jsFixedFastJsonLongIdKey.getLongId()
            );
        }
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
