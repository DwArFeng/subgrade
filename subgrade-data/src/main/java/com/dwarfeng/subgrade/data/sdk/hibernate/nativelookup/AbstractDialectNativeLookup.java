package com.dwarfeng.subgrade.data.sdk.hibernate.nativelookup;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;

import java.sql.Connection;
import java.util.Objects;

/**
 * DialectNativeLookup 的抽象实现。
 *
 * @author DwArFeng
 * @since 1.2.8
 */
public abstract class AbstractDialectNativeLookup<PE extends Bean> implements DialectNativeLookup<PE> {

    protected final String supportDialect;

    public AbstractDialectNativeLookup(String supportDialect) {
        this.supportDialect = supportDialect;
    }

    @Override
    public boolean supportDialect(String dialectClassCanonicalName) {
        return Objects.equals(supportDialect, dialectClassCanonicalName);
    }

    @Override
    public void init(Connection connection) {
        // Do nothing.
    }
}
