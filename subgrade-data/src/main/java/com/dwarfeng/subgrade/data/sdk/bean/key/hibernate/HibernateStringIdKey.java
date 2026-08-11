package com.dwarfeng.subgrade.data.sdk.bean.key.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import java.io.Serial;
import java.util.Objects;

/**
 * 适用于 Hibernate 的 StringIdKey。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateStringIdKey implements Key {

    @Serial
    private static final long serialVersionUID = 1915049752960836205L;

    private String stringId;

    public HibernateStringIdKey() {
    }

    public HibernateStringIdKey(String id) {
        this.stringId = id;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HibernateStringIdKey that = (HibernateStringIdKey) o;

        return Objects.equals(stringId, that.stringId);
    }

    @Override
    public int hashCode() {
        return stringId != null ? stringId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HibernateStringIdKey{" +
                "stringId='" + stringId + '\'' +
                '}';
    }
}
