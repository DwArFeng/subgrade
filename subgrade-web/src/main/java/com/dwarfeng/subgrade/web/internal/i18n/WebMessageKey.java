package com.dwarfeng.subgrade.web.internal.i18n;

import static com.dwarfeng.subgrade.web.internal.i18n.WebMessages.Catalog.*;

/**
 * Web 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum WebMessageKey {

    EXCEPTION_EXPRESSION_PARSER_MISSING(SDK, "exception.expression_parser_missing"),
    EXCEPTION_LOGIN_FAILED_DETAIL(STACK, "exception.login_failed_detail"),
    EXCEPTION_PERMISSION_DENIED_DETAIL(STACK, "exception.permission_denied_detail"),
    LOG_EXPRESSION_DISABLED_HINT(SDK, "log.expression_disabled_hint"),
    LOG_EXPRESSION_PARSER_MISSING(SDK, "log.expression_parser_missing"),
    LOG_FRIENDLY_FAILED(IMPL, "log.friendly_failed"),
    LOG_LOGIN_POSTPONE(SDK, "log.login_postpone"),
    LOG_VALIDATION_UNEXPECTED(SDK, "log.validation_unexpected"),
    SERVICE_EXCEPTION_LOGIN_FAILED(SDK, "service_exception.login_failed"),
    SERVICE_EXCEPTION_PARAM_VALIDATION_FAILED(SDK, "service_exception.param_validation_failed"),
    SERVICE_EXCEPTION_PERMISSION_DENIED(SDK, "service_exception.permission_denied");

    private final WebMessages.Catalog catalog;
    private final String key;

    WebMessageKey(WebMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    WebMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
