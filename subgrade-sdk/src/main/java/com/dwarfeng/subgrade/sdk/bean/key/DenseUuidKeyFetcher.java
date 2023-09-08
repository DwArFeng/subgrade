package com.dwarfeng.subgrade.sdk.bean.key;

import com.dwarfeng.dutil.basic.str.UUIDUtil;
import com.dwarfeng.subgrade.stack.bean.key.DenseUuidKey;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;

import java.util.UUID;

/**
 * 紧凑型 UUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 * @deprecated 该类不再推荐使用，请使用 impl 模块的相应主键生成器。
 */
@Deprecated
public class DenseUuidKeyFetcher implements KeyFetcher<DenseUuidKey> {

    @Override
    public DenseUuidKey fetchKey() {
        return new DenseUuidKey(UUIDUtil.toDenseString(UUID.randomUUID()));
    }
}
