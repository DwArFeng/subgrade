package com.dwarfeng.subgrade.stack.bean.key;

/**
 * Integer主键，封装了Integer。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class IntegerIdKey implements Key {

    private static final long serialVersionUID = 218645896865105669L;

    private int integerId;

    public IntegerIdKey() {
    }

    public IntegerIdKey(int integerId) {
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

        IntegerIdKey that = (IntegerIdKey) o;

        return integerId == that.integerId;
    }

    @Override
    public int hashCode() {
        return integerId;
    }

    @Override
    public String toString() {
        return "IntegerIdKey{" +
                "integerId=" + integerId +
                '}';
    }
}
