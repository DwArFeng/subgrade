package com.dwarfeng.subgrade.stack.bean.key;

/**
 * Long主键，封装了Long。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class LongIdKey implements Key {

    private static final long serialVersionUID = 4894711608426921844L;

    private long longId;

    public LongIdKey() {
    }

    public LongIdKey(long id) {
        this.longId = id;
    }

    public long getLongId() {
        return longId;
    }

    public void setLongId(long longId) {
        this.longId = longId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongIdKey longIdKey = (LongIdKey) o;

        return longId == longIdKey.longId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(longId);
    }

    @Override
    public String toString() {
        return "LongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
