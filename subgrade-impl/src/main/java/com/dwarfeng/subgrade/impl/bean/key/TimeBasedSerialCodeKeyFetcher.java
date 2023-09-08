package com.dwarfeng.subgrade.impl.bean.key;

import com.dwarfeng.subgrade.impl.generation.TimeBasedSerialCodeKeyGenerator;
import com.dwarfeng.subgrade.stack.bean.key.KeyFetcher;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.KeyFetchException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 使用 Redis 实现的序列号代码生成器。
 *
 * @author DwArFeng
 * @since 1.1.6
 * @deprecated 使用 {@link TimeBasedSerialCodeKeyGenerator} 代替。
 */
@Deprecated
public class TimeBasedSerialCodeKeyFetcher implements KeyFetcher<StringIdKey> {

    private final TimeBasedSerialCodeKeyGenerator keyGenerator;

    public TimeBasedSerialCodeKeyFetcher(
            @Nonnull RedisTemplate<String, Long> template,
            @Nonnull String keyPrefix,
            @Nonnull String serialCodeFormat,
            long indexResetPeriod
    ) {
        this.keyGenerator = new TimeBasedSerialCodeKeyGenerator(
                template, keyPrefix, serialCodeFormat, indexResetPeriod
        );
    }

    public TimeBasedSerialCodeKeyFetcher(
            @Nonnull RedisTemplate<String, Long> template,
            @Nonnull String keyPrefix,
            @Nonnull String serialCodeFormat,
            long indexResetPeriod,
            long initialValue,
            int step,
            double timeoutCoefficient
    ) {
        this.keyGenerator = new TimeBasedSerialCodeKeyGenerator(
                template, keyPrefix, serialCodeFormat, indexResetPeriod, initialValue, step, timeoutCoefficient
        );
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
        return "TimeBasedSerialCodeKeyFetcher{" +
                "keyGenerator=" + keyGenerator +
                '}';
    }
}
