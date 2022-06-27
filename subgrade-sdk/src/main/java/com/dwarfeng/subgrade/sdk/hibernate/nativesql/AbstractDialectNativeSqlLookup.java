package com.dwarfeng.subgrade.sdk.hibernate.nativesql;

import com.dwarfeng.subgrade.stack.bean.Bean;

import java.sql.Connection;
import java.util.Objects;

/**
 * DialectNativeSqlLookup 的抽象实现。
 *
 * @author DwArFeng
 * @since 1.2.8
 */
public abstract class AbstractDialectNativeSqlLookup<PE extends Bean> implements DialectNativeSqlLookup<PE> {

    protected final String supportDialect;

    public AbstractDialectNativeSqlLookup(String supportDialect) {
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
