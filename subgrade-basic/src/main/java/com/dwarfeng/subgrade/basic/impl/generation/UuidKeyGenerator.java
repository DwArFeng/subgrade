package com.dwarfeng.subgrade.basic.impl.generation;

import com.dwarfeng.subgrade.basic.stack.bean.key.UuidKey;
import com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator;

import java.util.UUID;

/**
 * UUID 主键生成器。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class UuidKeyGenerator implements KeyGenerator<UuidKey> {

    @Override
    public UuidKey generate() {
        return new UuidKey(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return "UuidKeyGenerator{}";
    }
}
