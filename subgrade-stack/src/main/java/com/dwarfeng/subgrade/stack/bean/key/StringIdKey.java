package com.dwarfeng.subgrade.stack.bean.key;

import java.util.Objects;

/**
 * String主键，封装了String。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class StringIdKey implements Key {

    private static final long serialVersionUID = -6993449616016427834L;

    private String stringId;

    public StringIdKey() {
    }

    public StringIdKey(String id) {
        this.stringId = id;
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

        StringIdKey that = (StringIdKey) o;

        return Objects.equals(stringId, that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId != null ? stringId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "StringIdKey{" +
                "stringId='" + stringId + '\'' +
                '}';
    }
}
