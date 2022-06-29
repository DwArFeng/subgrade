package com.dwarfeng.subgrade.sdk.hibernate.nativelookup;

import com.dwarfeng.subgrade.stack.bean.Bean;
import org.hibernate.dialect.Dialect;

/**
 * 针对特定方言的本地 SQL 查询。
 *
 * @author DwArFeng
 * @since 1.2.8
 */
public interface DialectNativeLookup<PE extends Bean> extends NativeLookup<PE> {

    /**
     * 返回此本地查询是否支持某个方言。
     *
     * @param dialectClassCanonicalName 方言类的规范名称
     * @return 是否支持此方言。
     */
    boolean supportDialect(String dialectClassCanonicalName);

    /**
     * 返回此本地查询是否支持某个方言。
     *
     * @param dialectClass 方言的类。
     * @return 是否支持此方言。
     */
    default boolean supportDialect(Class<? extends Dialect> dialectClass) {
        return supportDialect(dialectClass.getCanonicalName());
    }
}
