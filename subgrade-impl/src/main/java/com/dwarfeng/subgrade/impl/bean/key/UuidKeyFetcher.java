package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;

import java.util.UUID;

/**
 * UUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class UuidKeyFetcher implements KeyFetcher<UuidKey> {

    @Override
    public UuidKey fetchKey() {
        return new UuidKey(UUID.randomUUID().toString());
    }
}
