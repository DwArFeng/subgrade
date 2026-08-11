package com.dwarfeng.subgrade.web.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Web 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class WebMessagesTest {

    @Test
    public void shouldResolveStackMessages() {
        assertMessagesResolve(WebMessages.Catalog.STACK);
    }

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(WebMessages.Catalog.SDK);
    }

    @Test
    public void shouldResolveImplMessages() {
        assertMessagesResolve(WebMessages.Catalog.IMPL);
    }

    private void assertMessagesResolve(WebMessages.Catalog catalog) {
        for (WebMessageKey key : WebMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", WebMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", WebMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
