package com.dwarfeng.subgrade.stack.bean.key;

import java.util.Objects;

/**
 * 特殊的String主键，封装了使用Base64编码压缩过的UUID主键。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DenseUuidKey implements Key {

    private static final long serialVersionUID = 4581891864042654555L;

    private String uuid;

    public DenseUuidKey() {
    }

    public DenseUuidKey(String id) {
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

        DenseUuidKey that = (DenseUuidKey) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DenseUuidKey{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
