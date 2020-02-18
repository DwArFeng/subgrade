package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 适用于 FastJson 的 UuidKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonUuidKey implements Bean {

    private static final long serialVersionUID = 1819020183548760478L;

    /**
     * 根据指定的 UuidKey 生成 FastJsonUuidKey。
     *
     * @param uuidKey 指定的 UuidKey。
     * @return 通过指定的 UuidKey 生成的 FastJsonUuidKey。
     */
    public static FastJsonUuidKey of(@NonNull UuidKey uuidKey) {
        return new FastJsonUuidKey(uuidKey.getUuid());
    }


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
