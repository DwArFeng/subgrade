package com.dwarfeng.subgrade.basic.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Basic 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class BasicMessagesTest {

    @Test
    public void shouldResolveStackMessages() {
        assertMessagesResolve(BasicMessages.Catalog.STACK);
    }

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(BasicMessages.Catalog.SDK);
    }

    private void assertMessagesResolve(BasicMessages.Catalog catalog) {
        for (BasicMessageKey key : BasicMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", BasicMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", BasicMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
