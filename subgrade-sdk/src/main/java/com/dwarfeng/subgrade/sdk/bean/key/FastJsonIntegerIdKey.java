package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.IntegerIdKey;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.Objects;

/**
 * 适用于 FastJson 的 IntegerIdKey。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class FastJsonIntegerIdKey implements Key {

    private static final long serialVersionUID = 7101152716367669917L;

    /**
     * 根据指定的 IntegerIdKey 生成 FastJsonIntegerIdKey。
     *
     * @param integerIdKey 指定的 IntegerIdKey。
     * @return 通过指定的 IntegerIdKey 生成的 FastJsonIntegerIdKey。
     */
    public static FastJsonIntegerIdKey of(IntegerIdKey integerIdKey) {
        if (Objects.isNull(integerIdKey)) {
            return null;
        }
        return new FastJsonIntegerIdKey(integerIdKey.getIntegerId());
    }

    /**
     * 根据指定的 FastJsonIntegerIdKey 生成 IntegerIdKey。
     *
     * @param fastJsonIntegerIdKey 指定的 FastJsonIntegerIdKey。
     * @return 通过指定的 FastJsonIntegerIdKey 生成的 IntegerIdKey。
     * @since 1.2.13
     */
    public static IntegerIdKey toStackBean(FastJsonIntegerIdKey fastJsonIntegerIdKey) {
        if (Objects.isNull(fastJsonIntegerIdKey)) {
            return null;
        } else {
            return new IntegerIdKey(
                    fastJsonIntegerIdKey.getIntegerId()
            );
        }
    }

    @JSONField(name = "integer_id", ordinal = 1)
    private int integerId;

    public FastJsonIntegerIdKey() {
    }

    public FastJsonIntegerIdKey(int integerId) {
        this.integerId = integerId;
    }

    public int getIntegerId() {
        return integerId;
    }

    public void setIntegerId(int integerId) {
        this.integerId = integerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastJsonIntegerIdKey that = (FastJsonIntegerIdKey) o;

        return integerId == that.integerId;
    }

    @Override
    public int hashCode() {
        return integerId;
    }

    @Override
    public String toString() {
        return "FastJsonIntegerIdKey{" +
                "integerId=" + integerId +
                '}';
    }
}
