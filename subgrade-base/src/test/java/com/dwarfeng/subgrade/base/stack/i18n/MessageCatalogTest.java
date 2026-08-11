package com.dwarfeng.subgrade.base.stack.i18n;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 消息目录测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class MessageCatalogTest {

    @Test
    public void shouldCreateCatalogUsingAnchorModule() {
        MessageCatalog catalog = MessageCatalog.of(MessageCatalogTest.class, "subgrade-base.messages");

        assertEquals(MessageCatalogTest.class.getModule(), catalog.module());
        assertEquals("subgrade-base.messages", catalog.baseName());
    }

    @Test
    public void shouldRejectBlankBaseName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MessageCatalog(MessageCatalogTest.class.getModule(), " ")
        );
    }
}
