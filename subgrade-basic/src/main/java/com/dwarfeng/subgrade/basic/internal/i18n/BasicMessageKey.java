package com.dwarfeng.subgrade.basic.internal.i18n;

import static com.dwarfeng.subgrade.basic.internal.i18n.BasicMessages.Catalog.SDK;
import static com.dwarfeng.subgrade.basic.internal.i18n.BasicMessages.Catalog.STACK;

/**
 * Basic 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum BasicMessageKey {

    SERVICE_EXCEPTION_UNDEFINED(SDK, "service_exception.undefined"),
    SERVICE_EXCEPTION_IO(SDK, "service_exception.io"),
    SERVICE_EXCEPTION_PROCESS_FAILED(SDK, "service_exception.process_failed"),
    SERVICE_EXCEPTION_HANDLER_FAILED(SDK, "service_exception.handler_failed"),
    SERVICE_EXCEPTION_NOT_IMPLEMENTED(SDK, "service_exception.not_implemented"),
    SERVICE_EXCEPTION_GENERATE_FAILED(SDK, "service_exception.generate_failed"),
    SERVICE_EXCEPTION_PAGING_FAILED(SDK, "service_exception.paging_failed"),
    SERVICE_EXCEPTION_MESSAGE(SDK, "service_exception.message"),
    PAGING_EXCEPTION_DETAIL(STACK, "paging_exception.detail");

    private final BasicMessages.Catalog catalog;
    private final String key;

    BasicMessageKey(BasicMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    BasicMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
