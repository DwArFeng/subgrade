package com.dwarfeng.subgrade.stack.bean.key;

import java.util.Objects;

/**
 * Long主键，封装了Long。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class LongIdKey implements Key {

    private static final long serialVersionUID = 4894711608426921844L;

    private long guid;

    public LongIdKey() {
    }

    public LongIdKey(long guid) {
        this.guid = guid;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongIdKey guidKey = (LongIdKey) o;
        return guid == guidKey.guid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid);
    }

    @Override
    public String toString() {
        return "GuidKey{" +
                "guid=" + guid +
                '}';
    }
}
