package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Objects;

/**
 * 适用于 Hibernate 的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateStringIdKey implements Bean {

    private static final long serialVersionUID = -5726485840245744787L;

    private String id;

    public HibernateStringIdKey() {
    }

    public HibernateStringIdKey(String id) {
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

        HibernateStringIdKey that = (HibernateStringIdKey) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HibernateStringIdKey{" +
                "id='" + id + '\'' +
                '}';
    }
}
