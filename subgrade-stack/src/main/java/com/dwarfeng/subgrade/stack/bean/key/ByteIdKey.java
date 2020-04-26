package com.dwarfeng.subgrade.stack.bean.key;

/**
 * Byte主键，封装了Byte。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ByteIdKey implements Key {

    private static final long serialVersionUID = -5975700035199063806L;

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
