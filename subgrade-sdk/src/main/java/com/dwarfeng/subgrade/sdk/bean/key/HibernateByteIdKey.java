package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 适用于 Hibernate 的 ByteIdKey。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class HibernateByteIdKey implements Key {

    private static final long serialVersionUID = -1011434913027155759L;

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
