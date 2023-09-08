package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.exception.GenerateException;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * KeyFetcher 适配器帮助类。
 *
 * <p>
 * 该类用于处理过期的 {@link KeyFetcher} 接口，不应用于其它场景。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
@Deprecated
class KeyFetcherAdaptHelper {

    /**
     * 将指定的 keyFetcher 转换为 keyGenerator。
     *
     * @param keyFetcher 指定的 keyFetcher。
     * @param <K>        key 的类型。
     * @return 指定的 keyFetcher 转换而来的 keyGenerator。
     */
    public static <K extends Key> KeyGenerator<K> toKeyGenerator(@Nonnull KeyFetcher<K> keyFetcher) {
        return new KeyFetcherKeyGenerator<>(keyFetcher);
    }

    static class KeyFetcherKeyGenerator<K extends Key> implements KeyGenerator<K> {

        @Nonnull
        private final KeyFetcher<K> keyFetcher;

        public KeyFetcherKeyGenerator(@Nonnull KeyFetcher<K> keyFetcher) {
            this.keyFetcher = keyFetcher;
        }

        @Override
        public K generate() throws GenerateException {
            try {
                return keyFetcher.fetchKey();
            } catch (Exception e) {
                throw new GenerateException(e);
            }
        }

        @Override
        public List<K> batchGenerate(int size) throws GenerateException {
            try {
                return keyFetcher.batchFetchKey(size);
            } catch (Exception e) {
                throw new GenerateException(e);
            }
        }

        @Override
        public String toString() {
            return "KeyFetcherKeyGenerator{" +
                    "keyFetcher=" + keyFetcher +
                    '}';
        }
    }

    public static <K extends Key> KeyFetcher<K> toKeyFetcher(@Nonnull KeyGenerator<K> keyGenerator) {
        return new KeyGeneratorKeyFetcher<>(keyGenerator);
    }

    static class KeyGeneratorKeyFetcher<K extends Key> implements KeyFetcher<K> {

        @Nonnull
        private final KeyGenerator<K> keyGenerator;

        public KeyGeneratorKeyFetcher(@Nonnull KeyGenerator<K> keyGenerator) {
            this.keyGenerator = keyGenerator;
        }

        @Override
        public K fetchKey() throws KeyFetchException {
            try {
                return keyGenerator.generate();
            } catch (Exception e) {
                throw new KeyFetchException(e);
            }
        }

        @Override
        public List<K> batchFetchKey(int size) throws KeyFetchException {
            try {
                return keyGenerator.batchGenerate(size);
            } catch (Exception e) {
                throw new KeyFetchException(e);
            }
        }

        @Override
        public String toString() {
            return "KeyGeneratorKeyFetcher{" +
                    "keyGenerator=" + keyGenerator +
                    '}';
        }
    }
}
