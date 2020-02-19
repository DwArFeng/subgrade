package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.Bean;

/**
 * 适用于 Hibernate 的 IntegerIdKey。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class HibernateIntegerIdKey implements Bean {

    private static final long serialVersionUID = -3247707695336724271L;
    
    private int integerId;

    public HibernateIntegerIdKey() {
    }

    public HibernateIntegerIdKey(int integerId) {
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

        HibernateIntegerIdKey that = (HibernateIntegerIdKey) o;

        return integerId == that.integerId;
    }

    @Override
    public int hashCode() {
        return integerId;
    }

    @Override
    public String toString() {
        return "HibernateIntegerIdKey{" +
                "integerId=" + integerId +
                '}';
    }
}
