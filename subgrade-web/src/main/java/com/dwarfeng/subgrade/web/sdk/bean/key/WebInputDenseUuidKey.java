package com.dwarfeng.subgrade.web.sdk.bean.key;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dwarfeng.subgrade.basic.stack.bean.key.DenseUuidKey;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 DenseUuidKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputDenseUuidKey implements Key {

    @Serial
    private static final long serialVersionUID = -3595199149327272588L;
    @JSONField(name = "uuid")
    @NotNull
    @NotEmpty
    private String uuid;

    public WebInputDenseUuidKey() {
    }

    public WebInputDenseUuidKey(String id) {
        this.uuid = id;
    }

    /**
     * WebInputDenseUuidKey 转 DenseUuidKey。
     *
     * @param webInputDenseUuidKey WebInputDenseUuidKey。
     * @return DenseUuidKey。
     */
    public static DenseUuidKey toStackBean(WebInputDenseUuidKey webInputDenseUuidKey) {
        if (Objects.isNull(webInputDenseUuidKey)) {
            return null;
        }
        return new DenseUuidKey(webInputDenseUuidKey.getUuid());
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
