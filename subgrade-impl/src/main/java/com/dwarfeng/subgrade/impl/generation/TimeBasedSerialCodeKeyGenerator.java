package com.dwarfeng.subgrade.impl.generation;

import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.GenerateException;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 实现的序列号代码生成器。
 *
 * <p> 按照指定的格式生成流水号，流水号基于 {@link #serialCodeFormat}, 当前日期, 当前序号生成。
 * 当前序号是自增长的，每次调用一次方法，当前序号则会自动增长一个步长。
 *
 * <p> {@link #serialCodeFormat} 是流水号的格式，在该实例中，此字段以格式化文本的形式被调用。调用代码如下：<br>
 * <pre><code>String.format(serialCodeFormat, currentIndex, currentDate)</code></pre>
 * 通过改变 serialCodeFormat，则可生成符合规律的流水号。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class TimeBasedSerialCodeKeyGenerator implements KeyGenerator<StringIdKey> {

    public static final long INDEX_RESET_PERIOD_HOUR = 3600 * 1000;
    public static final long INDEX_RESET_PERIOD_DAY = 3600 * 1000 * 24;

    public static final long DEFAULT_INITIAL_VALUE = 0;
    public static final int DEFAULT_STEP = 1;
    public static final double DEFAULT_TIMEOUT_COEFFICIENT = 1.5;

    @Nonnull
    private RedisTemplate<String, Long> template;
    @Nonnull
    private String keyPrefix;
    @Nonnull
    private String serialCodeFormat;
    private long indexResetPeriod;
    long initialValue;
    private int step;
    private double timeoutCoefficient;

    public TimeBasedSerialCodeKeyGenerator(
            @Nonnull RedisTemplate<String, Long> template,
            @Nonnull String keyPrefix,
            @Nonnull String serialCodeFormat,
            long indexResetPeriod
    ) {
        this(
                template, keyPrefix, serialCodeFormat, indexResetPeriod,
                DEFAULT_INITIAL_VALUE, DEFAULT_STEP, DEFAULT_TIMEOUT_COEFFICIENT
        );
    }

    public TimeBasedSerialCodeKeyGenerator(
            @Nonnull RedisTemplate<String, Long> template,
            @Nonnull String keyPrefix,
            @Nonnull String serialCodeFormat,
            long indexResetPeriod,
            long initialValue,
            int step,
            double timeoutCoefficient
    ) {
        this.template = template;
        this.keyPrefix = keyPrefix;
        this.serialCodeFormat = serialCodeFormat;
        this.indexResetPeriod = indexResetPeriod;
        this.initialValue = initialValue;
        this.step = step;
        this.timeoutCoefficient = timeoutCoefficient;
    }

    @Override
    public StringIdKey generate() throws GenerateException {
        try {
            Date currentDate = new Date();
            String currentTimeKey = keyPrefix + (currentDate.getTime() / indexResetPeriod);
            Long currentIndex;
            boolean hasKey = Optional.of(template.hasKey(currentTimeKey)).orElse(false);
            if (!hasKey) {
                template.opsForValue().set(
                        currentTimeKey,
                        initialValue,
                        (long) (indexResetPeriod * timeoutCoefficient),
                        TimeUnit.MILLISECONDS
                );
                currentIndex = initialValue;
            } else {
                currentIndex = template.opsForValue().increment(currentTimeKey, step);
            }
            return new StringIdKey(String.format(serialCodeFormat, currentIndex, currentDate));
        } catch (Exception e) {
            throw new GenerateException(e);
        }
    }

    @Nonnull
    public RedisTemplate<String, Long> getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull RedisTemplate<String, Long> template) {
        this.template = template;
    }

    @Nonnull
    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(@Nonnull String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Nonnull
    public String getSerialCodeFormat() {
        return serialCodeFormat;
    }

    public void setSerialCodeFormat(@Nonnull String serialCodeFormat) {
        this.serialCodeFormat = serialCodeFormat;
    }

    public long getIndexResetPeriod() {
        return indexResetPeriod;
    }

    public void setIndexResetPeriod(long indexResetPeriod) {
        this.indexResetPeriod = indexResetPeriod;
    }

    public long getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(long initialValue) {
        this.initialValue = initialValue;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public double getTimeoutCoefficient() {
        return timeoutCoefficient;
    }

    public void setTimeoutCoefficient(double timeoutCoefficient) {
        this.timeoutCoefficient = timeoutCoefficient;
    }

    @Override
    public String toString() {
        return "TimeBasedSerialCodeKeyGenerator{" +
                "template=" + template +
                ", keyPrefix='" + keyPrefix + '\'' +
                ", serialCodeFormat='" + serialCodeFormat + '\'' +
                ", indexResetPeriod=" + indexResetPeriod +
                ", initialValue=" + initialValue +
                ", step=" + step +
                ", timeoutCoefficient=" + timeoutCoefficient +
                '}';
    }
}
