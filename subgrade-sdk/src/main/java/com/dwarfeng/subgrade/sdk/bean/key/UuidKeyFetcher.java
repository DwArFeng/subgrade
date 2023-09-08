package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;

import java.util.UUID;

/**
 * UUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 * @deprecated 该类不再推荐使用，请使用 impl 模块的相应主键生成器。
 */
@Deprecated
public class UuidKeyFetcher implements KeyFetcher<UuidKey> {

    @Override
    public UuidKey fetchKey() {
        return new UuidKey(UUID.randomUUID().toString());
    }
}
