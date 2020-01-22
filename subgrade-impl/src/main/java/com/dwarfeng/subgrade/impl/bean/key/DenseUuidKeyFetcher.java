package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.dutil.basic.str.UUIDUtil;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;

import java.util.UUID;

/**
 * DenseUUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class DenseUuidKeyFetcher implements KeyFetcher<UuidKey> {

    @Override
    public UuidKey fetchKey() {
        return new UuidKey(UUIDUtil.toDenseString(UUID.randomUUID()));
    }
}
