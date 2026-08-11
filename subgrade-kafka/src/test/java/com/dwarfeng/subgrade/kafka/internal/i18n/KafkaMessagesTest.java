package com.dwarfeng.subgrade.kafka.internal.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Kafka 模块消息入口测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class KafkaMessagesTest {

    @Test
    public void shouldResolveSdkMessages() {
        assertMessagesResolve(KafkaMessages.Catalog.SDK);
    }

    private void assertMessagesResolve(KafkaMessages.Catalog catalog) {
        for (KafkaMessageKey key : KafkaMessageKey.values()) {
            if (key.catalog() != catalog) {
                continue;
            }
            assertNotEquals("!" + key.key() + "!", KafkaMessages.message(Locale.ENGLISH, key), key.name());
            assertNotEquals("!" + key.key() + "!", KafkaMessages.message(Locale.SIMPLIFIED_CHINESE, key), key.name());
        }
    }
}
