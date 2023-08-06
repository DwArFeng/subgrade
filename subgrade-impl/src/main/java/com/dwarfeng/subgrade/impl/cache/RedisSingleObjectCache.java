package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.SingleObjectCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;

/**
 * 使用 Redis 实现的单对象缓存。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class RedisSingleObjectCache<K extends Key, E extends Entity<K>, JE extends Bean> implements SingleObjectCache<E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;
    private K key;

    public RedisSingleObjectCache(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer,
            @Nonnull K key) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
        this.key = key;
    }

    @Override
    public boolean exists() throws CacheException {
        try {
            return internalExists();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private boolean internalExists() {
        return template.hasKey(formatter.format(key));
    }

    @Override
    public E get() throws CacheException {
        try {
            JE je = template.opsForValue().get(formatter.format(key));
            return transformer.reverseTransform(je);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void put(E entity, long timeout) throws CacheException {
        try {
            JE je = transformer.transform(entity);
            template.opsForValue().set(formatter.format(key), je, timeout);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            template.delete(formatter.format(key));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull RedisTemplate<String, JE> template) {
        this.template = template;
    }

    public StringKeyFormatter<K> getFormatter() {
        return formatter;
    }

    public void setFormatter(@Nonnull StringKeyFormatter<K> formatter) {
        this.formatter = formatter;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@Nonnull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }

    public K getKey() {
        return key;
    }

    public void setKey(@Nonnull K key) {
        this.key = key;
    }
}
