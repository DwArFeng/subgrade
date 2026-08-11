package com.dwarfeng.subgrade.base.sdk.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消息语言环境上下文测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class MessageContextTest {

    @Test
    public void shouldBindAndRestoreScopedLocale() {
        assertFalse(MessageContext.isBound());

        Locale locale = MessageContext.call(Locale.ENGLISH, () -> {
            assertTrue(MessageContext.isBound());
            assertEquals(Locale.ENGLISH, MessageContext.currentLocale());
            return MessageContext.call(Locale.SIMPLIFIED_CHINESE, () -> {
                assertTrue(MessageContext.isBound());
                assertEquals(Locale.SIMPLIFIED_CHINESE, MessageContext.currentLocale());
                return MessageContext.currentLocale();
            });
        });

        assertEquals(Locale.SIMPLIFIED_CHINESE, locale);
        assertFalse(MessageContext.isBound());
    }
}
