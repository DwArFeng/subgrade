package com.dwarfeng.subgrade.stack.bean.key;

/**
 * Long主键，封装了Long。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class LongIdKey implements Key {

    private static final long serialVersionUID = 4894711608426921844L;

    private long id;

    public LongIdKey() {
    }

    public LongIdKey(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongIdKey longIdKey = (LongIdKey) o;

        return id == longIdKey.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "LongIdKey{" +
                "id=" + id +
                '}';
    }
}
