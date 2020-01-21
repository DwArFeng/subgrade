package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.Objects;

/**
 * 适用于 Hibernate 的 UuidKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateUuidKey implements Bean {

    private static final long serialVersionUID = 1819020183548760478L;

    private String uuid;

    public HibernateUuidKey() {
    }

    public HibernateUuidKey(String id) {
        this.uuid = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HibernateUuidKey that = (HibernateUuidKey) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HibernateUuidKey{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
