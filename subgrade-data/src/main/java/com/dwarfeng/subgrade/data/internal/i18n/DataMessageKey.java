package com.dwarfeng.subgrade.data.internal.i18n;

import static com.dwarfeng.subgrade.data.internal.i18n.DataMessages.Catalog.*;

/**
 * Data 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum DataMessageKey {

    EXCEPTION_ENTITY_EXISTED_DETAIL(STACK, "exception.entity_existed_detail"),
    EXCEPTION_ENTITY_NOT_EXIST_DETAIL(STACK, "exception.entity_not_exist_detail"),
    EXCEPTION_FAST_JSON_DESERIALIZATION_FAILED(SDK, "exception.fast_json_deserialization_failed"),
    LOG_PAGING_PAGE_FIXED(IMPL, "log.paging_page_fixed"),
    LOG_PAGING_ROWS_FIXED(IMPL, "log.paging_rows_fixed"),
    SERVICE_EXCEPTION_CACHE_FAILED(SDK, "service_exception.cache_failed"),
    SERVICE_EXCEPTION_DAO_FAILED(SDK, "service_exception.dao_failed"),
    SERVICE_EXCEPTION_DATABASE_FAILED(SDK, "service_exception.database_failed"),
    SERVICE_EXCEPTION_ENTITY_EXISTED(SDK, "service_exception.entity_existed"),
    SERVICE_EXCEPTION_ENTITY_NOT_EXIST(SDK, "service_exception.entity_not_exist");

    private final DataMessages.Catalog catalog;
    private final String key;

    DataMessageKey(DataMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    DataMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
