package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Objects;

/**
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonUuidKey implements Bean {

    private static final long serialVersionUID = 1819020183548760478L;

    @JSONField(name = "uuid", ordinal = 1)
    private String uuid;

    public FastJsonUuidKey() {
    }

    public FastJsonUuidKey(String id) {
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

        FastJsonUuidKey that = (FastJsonUuidKey) o;

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
