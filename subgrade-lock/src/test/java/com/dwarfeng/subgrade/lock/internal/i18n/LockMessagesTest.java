package com.dwarfeng.subgrade.lock.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Lock 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class LockMessagesTest {

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(LockMessages.Catalog.SDK);
    }

    @Test
    public void shouldResolveImplMessages() {
        assertMessagesResolve(LockMessages.Catalog.IMPL);
    }

    private void assertMessagesResolve(LockMessages.Catalog catalog) {
        for (LockMessageKey key : LockMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", LockMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", LockMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
