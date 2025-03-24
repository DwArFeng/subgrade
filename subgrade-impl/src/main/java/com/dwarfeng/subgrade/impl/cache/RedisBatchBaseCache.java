package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.*;
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
public class RedisBatchBaseCache<K extends Key, E extends Entity<K>, JE extends Bean> implements BatchBaseCache<K, E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;

    public RedisBatchBaseCache(
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
            return internalExists(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public E get(K key) throws CacheException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void push(E value, long timeout) throws CacheException {
        try {
            internalPush(value, timeout);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void delete(K key) throws CacheException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            Set<String> keys = Optional.of(template.keys(formatter.generalFormat())).orElse(Collections.emptySet());
            template.delete(keys);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private boolean internalExists(K key) {
        // 获得装箱后的结果，拆箱并返回。
        return template.hasKey(formatKey(key));
    }

    private E internalGet(K key) {
        JE je = template.opsForValue().get(formatKey(key));
        return transformer.reverseTransform(je);
    }

    private void internalPush(E value, long timeout) {
        template.opsForValue().set(
                formatKey(value.getKey()),
                transformer.transform(value),
                timeout, TimeUnit.MILLISECONDS
        );
    }

    private void internalDelete(K key) {
        template.delete(formatKey(key));
    }

    @Override
    public boolean allExists(List<K> keys) throws CacheException {
        try {
            for (K key : keys) {
                if (!internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean nonExists(List<K> keys) throws CacheException {
        try {
            for (K key : keys) {
                if (internalExists(key)) return false;
            }
            return true;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public List<E> batchGet(List<K> keys) throws CacheException {
        try {
            List<E> entities = new ArrayList<>();
            for (K key : keys) {
                entities.add(internalGet(key));
            }
            return entities;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void batchPush(List<E> entities, long timeout) throws CacheException {
        try {
            for (E entity : entities) {
                internalPush(entity, timeout);
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws CacheException {
        try {
            for (K key : keys) {
                internalDelete(key);
            }
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
        return "RedisBatchBaseCache{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                '}';
    }
}
