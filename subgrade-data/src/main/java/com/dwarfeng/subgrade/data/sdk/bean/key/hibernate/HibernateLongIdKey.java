package com.dwarfeng.subgrade.data.sdk.bean.key.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import java.io.Serial;

/**
 * 适用于 Hibernate 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateLongIdKey implements Key {

    @Serial
    private static final long serialVersionUID = -1169447604298106953L;

    private long longId;

    public HibernateLongIdKey() {
    }

    public HibernateLongIdKey(long longId) {
        this.longId = longId;
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

        HibernateLongIdKey that = (HibernateLongIdKey) o;

        return longId == that.longId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(longId);
    }

    @Override
    public String toString() {
        return "HibernateLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
