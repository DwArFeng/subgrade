package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 适用于 Hibernate 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateLongIdKey implements Key {

    private static final long serialVersionUID = 8863415673517584070L;

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
