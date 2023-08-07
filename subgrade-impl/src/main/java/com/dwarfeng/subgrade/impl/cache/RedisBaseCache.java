package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BaseCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 实现的 BaseCache。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class RedisBaseCache<K extends Key, E extends Entity<K>, JE extends Bean> implements BaseCache<K, E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;

    public RedisBaseCache(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer
    ) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
    }

    @Override
    public boolean exists(K key) throws CacheException {
        try {
            // 获得装箱结果。
            Boolean result = template.hasKey(formatKey(key));
            // 拆箱并返回。
            return result != null && result;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public E get(K key) throws CacheException {
        try {
            JE je = template.opsForValue().get(formatKey(key));
            return transformer.reverseTransform(je);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void push(E value, long timeout) throws CacheException {
        try {
            template.opsForValue().set(
                    formatKey(value.getKey()),
                    transformer.transform(value),
                    timeout, TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void delete(K key) throws CacheException {
        try {
            template.delete(formatKey(key));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            Set<String> keys = Optional.ofNullable(
                    template.keys(formatter.generalFormat())
            ).orElse(Collections.emptySet());
            template.delete(keys);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private String formatKey(K key) {
        return formatter.format(key);
    }

    @Nonnull
    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull RedisTemplate<String, JE> template) {
        this.template = template;
    }

    @Nonnull
    public StringKeyFormatter<K> getFormatter() {
        return formatter;
    }

    public void setFormatter(@Nonnull StringKeyFormatter<K> formatter) {
        this.formatter = formatter;
    }

    @Nonnull
    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@Nonnull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }

    @Override
    public String toString() {
        return "RedisBaseCache{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                '}';
    }
}
