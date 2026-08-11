package com.dwarfeng.subgrade.cache.internal.i18n;

import static com.dwarfeng.subgrade.cache.internal.i18n.CacheMessages.Catalog.IMPL;
import static com.dwarfeng.subgrade.cache.internal.i18n.CacheMessages.Catalog.SDK;

/**
 * Cache 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum CacheMessageKey {

    SERVICE_EXCEPTION_OPERATION_FAILED(SDK, "service_exception.operation_failed"),
    LOG_FETCH_SUCCEEDED(IMPL, "log.fetch_succeeded"),
    LOG_CLEAR_KEY(IMPL, "log.clear_key"),
    LOG_CLEAR_ALL(IMPL, "log.clear_all"),
    LOG_EXPIRE_SCAN_STARTED(IMPL, "log.expire_scan_started"),
    LOG_EXPIRED_REMOVED(IMPL, "log.expired_removed"),
    LOG_NO_EXPIRED(IMPL, "log.no_expired");

    private final CacheMessages.Catalog catalog;
    private final String key;

    CacheMessageKey(CacheMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    CacheMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
