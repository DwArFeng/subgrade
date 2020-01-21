package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 适用于 FastJson 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class FastJsonLongIdKey implements Bean {

    private static final long serialVersionUID = 8863415673517584070L;

    private long id;

    public FastJsonLongIdKey() {
    }

    public FastJsonLongIdKey(long id) {
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

        FastJsonLongIdKey that = (FastJsonLongIdKey) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "FastJsonLongIdKey{" +
                "id=" + id +
                '}';
    }
}
