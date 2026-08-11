package com.dwarfeng.subgrade.data.sdk.bean.key.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.key.Key;

import java.io.Serial;

/**
 * 适用于 Hibernate 的 ByteIdKey。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class HibernateByteIdKey implements Key {

    @Serial
    private static final long serialVersionUID = 521196337583902379L;

    private byte byteId;

    public HibernateByteIdKey() {
    }

    public HibernateByteIdKey(byte byteId) {
        this.byteId = byteId;
    }

    public byte getByteId() {
        return byteId;
    }

    public void setByteId(byte byteId) {
        this.byteId = byteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HibernateByteIdKey that = (HibernateByteIdKey) o;

        return byteId == that.byteId;
    }

    @Override
    public int hashCode() {
        return byteId;
    }

    @Override
    public String toString() {
        return "HibernateByteIdKey{" +
                "byteId=" + byteId +
                '}';
    }
}
