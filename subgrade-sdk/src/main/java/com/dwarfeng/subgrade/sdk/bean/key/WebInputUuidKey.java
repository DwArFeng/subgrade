package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 UuidKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputUuidKey implements Key {

    private static final long serialVersionUID = -2737801818336160820L;

    @JSONField(name = "uuid", ordinal = 1)
    @NotNull
    @NotEmpty
    private String uuid;

    public WebInputUuidKey() {
    }

    public WebInputUuidKey(String id) {
        this.uuid = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UuidKey toStackBean() {
        return new UuidKey(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebInputUuidKey that = (WebInputUuidKey) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebInputUuidKey{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
