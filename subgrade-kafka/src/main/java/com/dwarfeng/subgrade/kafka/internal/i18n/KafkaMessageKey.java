package com.dwarfeng.subgrade.kafka.internal.i18n;

import static com.dwarfeng.subgrade.kafka.internal.i18n.KafkaMessages.Catalog.SDK;

/**
 * Kafka 模块消息键。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public enum KafkaMessageKey {

    SERVICE_EXCEPTION_SERIALIZATION_FAILED(SDK, "service_exception.serialization_failed"),
    FAST_JSON_DESERIALIZATION_FAILED(SDK, "fast_json.deserialization_failed");

    private final KafkaMessages.Catalog catalog;
    private final String key;

    KafkaMessageKey(KafkaMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    KafkaMessages.Catalog catalog() {
        return catalog;
    }

    public String key() {
        return key;
    }
}
