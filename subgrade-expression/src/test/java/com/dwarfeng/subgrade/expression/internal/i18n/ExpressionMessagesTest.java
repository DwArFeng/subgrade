package com.dwarfeng.subgrade.expression.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Expression 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ExpressionMessagesTest {

    @Test
    public void shouldResolveStackMessages() {
        assertMessagesResolve(ExpressionMessages.Catalog.STACK);
    }

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(ExpressionMessages.Catalog.SDK);
    }

    private void assertMessagesResolve(ExpressionMessages.Catalog catalog) {
        for (ExpressionMessageKey key : ExpressionMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", ExpressionMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals(
                    "!" + key.key() + "!", ExpressionMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name()
            );
        }
    }
}
