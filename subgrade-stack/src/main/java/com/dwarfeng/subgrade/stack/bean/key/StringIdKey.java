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

    private String id;

    public StringIdKey() {
    }

    public StringIdKey(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringIdKey that = (StringIdKey) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "StringIdKey{" +
                "id='" + id + '\'' +
                '}';
    }
}
