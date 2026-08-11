package com.dwarfeng.subgrade.lock.internal.i18n;

import static com.dwarfeng.subgrade.lock.internal.i18n.LockMessages.Catalog.IMPL;
import static com.dwarfeng.subgrade.lock.internal.i18n.LockMessages.Catalog.SDK;

/**
 * Lock 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum LockMessageKey {

    SERVICE_EXCEPTION_OPERATION_FAILED(SDK, "service_exception.operation_failed"),
    LOG_ONLINE(IMPL, "log.online"),
    LOG_OFFLINE(IMPL, "log.offline"),
    LOG_START(IMPL, "log.start"),
    LOG_STOP(IMPL, "log.stop"),
    LOG_WORK(IMPL, "log.work"),
    LOG_REST(IMPL, "log.rest"),
    LOG_LOCK_HELD(IMPL, "log.lock_held"),
    LOG_LOCK_RELEASED(IMPL, "log.lock_released"),
    LOG_WORK_FAILED(IMPL, "log.work_failed"),
    LOG_REST_FAILED(IMPL, "log.rest_failed");

    private final LockMessages.Catalog catalog;
    private final String key;

    LockMessageKey(LockMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    LockMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
