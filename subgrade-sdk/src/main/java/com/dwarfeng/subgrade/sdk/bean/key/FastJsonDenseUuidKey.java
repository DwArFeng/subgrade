package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Objects;

/**
 * 适用于 FastJson 的 DenseUuidKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonDenseUuidKey implements Bean {

    private static final long serialVersionUID = -678760030501902949L;

    @JSONField(name = "uuid", ordinal = 1)
    private String uuid;

    public FastJsonDenseUuidKey() {
    }

    public FastJsonDenseUuidKey(String id) {
        this.uuid = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastJsonDenseUuidKey that = (FastJsonDenseUuidKey) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FastJsonUuidKey{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}