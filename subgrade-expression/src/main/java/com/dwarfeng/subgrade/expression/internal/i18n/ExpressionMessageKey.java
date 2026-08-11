package com.dwarfeng.subgrade.expression.internal.i18n;

import static com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessages.Catalog.SDK;
import static com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessages.Catalog.STACK;

/**
 * Expression 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum ExpressionMessageKey {

    SERVICE_EXCEPTION_PARSE_FAILED(SDK, "service_exception.parse_failed"),
    PARSE_FAILED_DETAIL(STACK, "expression.parse_failed_detail");

    private final ExpressionMessages.Catalog catalog;
    private final String key;

    ExpressionMessageKey(ExpressionMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    ExpressionMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
