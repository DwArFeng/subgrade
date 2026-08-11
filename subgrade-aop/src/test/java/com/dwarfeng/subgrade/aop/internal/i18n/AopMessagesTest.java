package com.dwarfeng.subgrade.aop.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * AOP 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class AopMessagesTest {

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(AopMessages.Catalog.SDK);
    }

    private void assertMessagesResolve(AopMessages.Catalog catalog) {
        for (AopMessageKey key : AopMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", AopMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", AopMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
