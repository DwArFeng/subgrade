package com.dwarfeng.subgrade.data.sdk.bean.key.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import java.io.Serial;
import java.util.Objects;

/**
 * 适用于 Hibernate 的 UuidKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateDenseUuidKey implements Key {

    @Serial
    private static final long serialVersionUID = -7342149646542175550L;

    private String uuid;

    public HibernateDenseUuidKey() {
    }

    public HibernateDenseUuidKey(String id) {
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

        HibernateDenseUuidKey that = (HibernateDenseUuidKey) o;

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
