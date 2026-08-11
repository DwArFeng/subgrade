package com.dwarfeng.subgrade.cache.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Cache 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class CacheMessagesTest {

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(CacheMessages.Catalog.SDK);
    }

    @Test
    public void shouldResolveImplMessages() {
        assertMessagesResolve(CacheMessages.Catalog.IMPL);
    }

    private void assertMessagesResolve(CacheMessages.Catalog catalog) {
        for (CacheMessageKey key : CacheMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", CacheMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", CacheMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
