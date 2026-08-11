package com.dwarfeng.subgrade.basic.stack.bean.key;

import java.io.Serial;
import java.util.Objects;

/**
 * String 主键，封装了 String。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class StringIdKey implements Key {

    @Serial
    private static final long serialVersionUID = 4004653075467370635L;

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
