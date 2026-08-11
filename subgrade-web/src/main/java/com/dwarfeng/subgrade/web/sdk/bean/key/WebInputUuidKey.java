package com.dwarfeng.subgrade.web.sdk.bean.key;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.bean.key.UuidKey;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 UuidKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputUuidKey implements Key {

    @Serial
    private static final long serialVersionUID = 8804151021285032084L;
    @JSONField(name = "uuid")
    @NotNull
    @NotEmpty
    private String uuid;

    public WebInputUuidKey() {
    }

    public WebInputUuidKey(String id) {
        this.uuid = id;
    }

    /**
     * WebInputUuidKey 转 UuidKey。
     *
     * @param webInputUuidKey WebInputUuidKey。
     * @return UuidKey。
     */
    public static UuidKey toStackBean(WebInputUuidKey webInputUuidKey) {
        if (Objects.isNull(webInputUuidKey)) {
            return null;
        }
        return new UuidKey(webInputUuidKey.getUuid());
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
