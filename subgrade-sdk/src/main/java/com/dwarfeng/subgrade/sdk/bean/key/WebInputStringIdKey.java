package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputStringIdKey implements Key {

    private static final long serialVersionUID = 5663915425535205270L;

    @JSONField(name = "string_id", ordinal = 1)
    @NotNull
    @NotEmpty
    private String stringId;

    public WebInputStringIdKey() {
    }

    public WebInputStringIdKey(String id) {
        this.stringId = id;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public StringIdKey toStackBean() {
        return new StringIdKey(stringId);
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
