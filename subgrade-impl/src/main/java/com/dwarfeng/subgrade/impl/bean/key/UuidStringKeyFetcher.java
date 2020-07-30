package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;

import java.util.UUID;

/**
 * UUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 1.1.3
 */
public class UuidStringKeyFetcher implements KeyFetcher<StringIdKey> {

    @Override
    public StringIdKey fetchKey() {
        return new StringIdKey(UUID.randomUUID().toString());
    }
}
