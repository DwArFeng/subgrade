package com.dwarfeng.subgrade.data.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Data 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class DataMessagesTest {

    @Test
    public void shouldResolveStackMessages() {
        assertMessagesResolve(DataMessages.Catalog.STACK);
    }

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(DataMessages.Catalog.SDK);
    }

    @Test
    public void shouldResolveImplMessages() {
        assertMessagesResolve(DataMessages.Catalog.IMPL);
    }

    private void assertMessagesResolve(DataMessages.Catalog catalog) {
        for (DataMessageKey key : DataMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", DataMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", DataMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
