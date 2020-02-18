package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 适用于 FastJson 的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonStringIdKey implements Bean {

    private static final long serialVersionUID = -2889799277375755227L;

    /**
     * 根据指定的 StringIdKey 生成 FastJsonStringIdKey。
     *
     * @param stringIdKey 指定的 StringIdKey。
     * @return 通过指定的 StringIdKey 生成的 FastJsonStringIdKey。
     */
    public static FastJsonStringIdKey of(@NonNull StringIdKey stringIdKey) {
        return new FastJsonStringIdKey(stringIdKey.getStringId());
    }

    @JSONField(name = "id", ordinal = 1)
    private String stringId;

    public FastJsonStringIdKey() {
    }

    public FastJsonStringIdKey(String id) {
        this.stringId = id;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastJsonStringIdKey that = (FastJsonStringIdKey) o;

        return Objects.equals(stringId, that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId != null ? stringId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FastJsonStringIdKey{" +
                "stringId='" + stringId + '\'' +
                '}';
    }
}
