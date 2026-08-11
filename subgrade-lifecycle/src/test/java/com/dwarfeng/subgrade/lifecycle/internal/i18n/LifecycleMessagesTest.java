package com.dwarfeng.subgrade.lifecycle.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Lifecycle 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class LifecycleMessagesTest {

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(LifecycleMessages.Catalog.SDK);
    }

    @Test
    public void shouldResolveImplMessages() {
        assertMessagesResolve(LifecycleMessages.Catalog.IMPL);
    }

    private void assertMessagesResolve(LifecycleMessages.Catalog catalog) {
        for (LifecycleMessageKey key : LifecycleMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", LifecycleMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals(
                    "!" + key.key() + "!", LifecycleMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name()
            );
        }
    }
}
