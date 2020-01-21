package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Objects;

/**
 * 适用于 FastJson 的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonStringIdKey implements Bean {

    private static final long serialVersionUID = -2889799277375755227L;

    @JSONField(name = "id", ordinal = 1)
    private String id;

    public FastJsonStringIdKey() {
    }

    public FastJsonStringIdKey(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastJsonStringIdKey that = (FastJsonStringIdKey) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FastJsonStringIdKey{" +
                "id='" + id + '\'' +
                '}';
    }
}
