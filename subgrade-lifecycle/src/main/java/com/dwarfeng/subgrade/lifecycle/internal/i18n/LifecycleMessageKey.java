package com.dwarfeng.subgrade.lifecycle.internal.i18n;

import static com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessages.Catalog.IMPL;
import static com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessages.Catalog.SDK;

/**
 * Lifecycle 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum LifecycleMessageKey {

    SERVICE_EXCEPTION_OPERATION_FAILED(SDK, "service_exception.operation_failed"),
    LOG_ONLINE(IMPL, "log.online"),
    LOG_OFFLINE(IMPL, "log.offline"),
    LOG_START(IMPL, "log.start"),
    LOG_STOP(IMPL, "log.stop");

    private final LifecycleMessages.Catalog catalog;
    private final String key;

    LifecycleMessageKey(LifecycleMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    LifecycleMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
