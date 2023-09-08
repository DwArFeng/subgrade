package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.impl.generation.UuidKeyGenerator;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.UuidKey;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

import java.util.List;

/**
 * UUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 * @deprecated 使用 {@link UuidKeyGenerator} 代替。
 */
@Deprecated
public class UuidKeyFetcher implements KeyFetcher<UuidKey> {

    private final UuidKeyGenerator keyGenerator;

    public UuidKeyFetcher() {
        this.keyGenerator = new UuidKeyGenerator();
    }

    @Override
    public UuidKey fetchKey() throws KeyFetchException {
        try {
            return keyGenerator.generate();
        } catch (Exception e) {
            throw new KeyFetchException(e);
        }
    }

    @Override
    public List<UuidKey> batchFetchKey(int size) throws KeyFetchException {
        try {
            return keyGenerator.batchGenerate(size);
        } catch (Exception e) {
            throw new KeyFetchException(e);
        }
    }

    @Override
    public String toString() {
        return "UuidKeyFetcher{" +
                "keyGenerator=" + keyGenerator +
                '}';
    }
}
