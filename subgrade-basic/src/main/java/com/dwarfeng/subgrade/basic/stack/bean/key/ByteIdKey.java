package com.dwarfeng.subgrade.basic.stack.bean.key;

import java.io.Serial;

/**
 * Byte 主键，封装了 Byte。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ByteIdKey implements Key {

    @Serial
    private static final long serialVersionUID = 2666509190410765026L;

    private byte byteId;

    public ByteIdKey() {
    }

    public ByteIdKey(byte byteId) {
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

        ByteIdKey byteIdKey = (ByteIdKey) o;

        return byteId == byteIdKey.byteId;
    }

    @Override
    public int hashCode() {
        return byteId;
    }

    @Override
    public String toString() {
        return "ByteIdKey{" +
                "byteId=" + byteId +
                '}';
    }
}
