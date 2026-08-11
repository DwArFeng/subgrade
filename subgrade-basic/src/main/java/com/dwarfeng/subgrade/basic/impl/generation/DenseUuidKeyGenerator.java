package com.dwarfeng.subgrade.basic.impl.generation;

import com.dwarfeng.dutil.basic.sdk.string.UUIDUtil;
import com.dwarfeng.subgrade.basic.stack.bean.key.UuidKey;
import com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator;

import java.util.UUID;

/**
 * DenseUUID 主键生成器。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class DenseUuidKeyGenerator implements KeyGenerator<UuidKey> {

    @Override
    public UuidKey generate() {
        return new UuidKey(UUIDUtil.toDenseString(UUID.randomUUID()));
    }

    @Override
    public String toString() {
        return "DenseUuidKeyGenerator{}";
    }
}
