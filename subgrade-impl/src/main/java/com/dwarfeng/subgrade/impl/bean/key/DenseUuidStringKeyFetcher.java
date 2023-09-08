package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.impl.generation.DenseUuidStringKeyGenerator;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;

import java.util.List;

/**
 * DenseUUID 主键抓取器。
 *
 * @author DwArFeng
 * @since 1.1.3
 * @deprecated 使用 {@link DenseUuidStringKeyGenerator} 代替。
 */
@Deprecated
public class DenseUuidStringKeyFetcher implements KeyFetcher<StringIdKey> {

    private final DenseUuidStringKeyGenerator keyGenerator;

    public DenseUuidStringKeyFetcher() {
        this.keyGenerator = new DenseUuidStringKeyGenerator();
    }

    @Override
    public StringIdKey fetchKey() throws KeyFetchException {
        try {
            return keyGenerator.generate();
        } catch (Exception e) {
            throw new KeyFetchException(e);
        }
    }

    @Override
    public List<StringIdKey> batchFetchKey(int size) throws KeyFetchException {
        try {
            return keyGenerator.batchGenerate(size);
        } catch (Exception e) {
            throw new KeyFetchException(e);
        }
    }

    @Override
    public String toString() {
        return "DenseUuidStringKeyFetcher{" +
                "keyGenerator=" + keyGenerator +
                '}';
    }
}
