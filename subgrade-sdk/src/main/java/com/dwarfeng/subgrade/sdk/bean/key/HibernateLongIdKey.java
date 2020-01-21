package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 适用于 Hibernate 的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateLongIdKey implements Bean {

    private static final long serialVersionUID = 8863415673517584070L;

    @JSONField(name = "id", ordinal = 1)
    private long id;

    public HibernateLongIdKey() {
    }

    public HibernateLongIdKey(long id) {
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

        HibernateLongIdKey that = (HibernateLongIdKey) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "HibernateLongIdKey{" +
                "id=" + id +
                '}';
    }
}
