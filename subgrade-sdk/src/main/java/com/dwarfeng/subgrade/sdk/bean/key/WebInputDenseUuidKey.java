package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.DenseUuidKey;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 DenseUuidKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputDenseUuidKey implements Key {

    private static final long serialVersionUID = -5816015187408098169L;

    @JSONField(name = "uuid", ordinal = 1)
    @NotNull
    @NotEmpty
    private String uuid;

    public WebInputDenseUuidKey() {
    }

    public WebInputDenseUuidKey(String id) {
        this.uuid = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DenseUuidKey toStackBean() {
        return new DenseUuidKey(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebInputDenseUuidKey that = (WebInputDenseUuidKey) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebInputDenseUuidKey{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
