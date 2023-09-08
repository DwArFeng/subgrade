package com.dwarfeng.subgrade.impl.generation;

import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;

import java.util.UUID;

/**
 * UUID 主键生成器。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class UuidStringKeyGenerator implements KeyGenerator<StringIdKey> {

    @Override
    public StringIdKey generate() {
        return new StringIdKey(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return "UuidStringKeyGenerator{}";
    }
}
