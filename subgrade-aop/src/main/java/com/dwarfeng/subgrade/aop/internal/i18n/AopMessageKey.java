package com.dwarfeng.subgrade.aop.internal.i18n;

import static com.dwarfeng.subgrade.aop.internal.i18n.AopMessages.Catalog.SDK;

/**
 * AOP 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum AopMessageKey {

    SERVICE_EXCEPTION_INTERCEPTION_FAILED(SDK, "service_exception.interception_failed");

    private final AopMessages.Catalog catalog;
    private final String key;

    AopMessageKey(AopMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    AopMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
