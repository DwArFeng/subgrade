package com.dwarfeng.subgrade.impl.generation;

import com.dwarfeng.dutil.basic.str.UUIDUtil;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;

import java.util.UUID;

/**
 * DenseUUID 主键生成器。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class DenseUuidStringKeyGenerator implements KeyGenerator<StringIdKey> {

    @Override
    public StringIdKey generate() {
        return new StringIdKey(UUIDUtil.toDenseString(UUID.randomUUID()));
    }

    @Override
    public String toString() {
        return "DenseUuidStringKeyGenerator{}";
    }
}
