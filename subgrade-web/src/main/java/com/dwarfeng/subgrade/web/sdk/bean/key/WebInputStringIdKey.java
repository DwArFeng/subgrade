package com.dwarfeng.subgrade.web.sdk.bean.key;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.bean.key.StringIdKey;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputStringIdKey implements Key {

    @Serial
    private static final long serialVersionUID = -4030563873563439577L;
    @JSONField(name = "string_id")
    @NotNull
    @NotEmpty
    private String stringId;

    public WebInputStringIdKey() {
    }

    public WebInputStringIdKey(String id) {
        this.stringId = id;
    }

    /**
     * WebInputStringIdKey 转 StringIdKey。
     *
     * @param webInputStringIdKey WebInputStringIdKey。
     * @return StringIdKey。
     */
    public static StringIdKey toStackBean(WebInputStringIdKey webInputStringIdKey) {
        if (Objects.isNull(webInputStringIdKey)) {
            return null;
        }
        return new StringIdKey(webInputStringIdKey.getStringId());
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

        WebInputStringIdKey that = (WebInputStringIdKey) o;

        return Objects.equals(stringId, that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId != null ? stringId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebInputStringIdKey{" +
                "stringId='" + stringId + '\'' +
                '}';
    }
}
