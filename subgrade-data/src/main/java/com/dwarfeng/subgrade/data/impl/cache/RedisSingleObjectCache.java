package com.dwarfeng.subgrade.data.impl.cache;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.data.stack.cache.SingleObjectCache;
import com.dwarfeng.subgrade.data.stack.exception.CacheException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 使用 Redis 实现的单对象缓存。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.5-beta
 */
public class RedisSingleObjectCache<K extends Key, E extends Entity<K>, JE extends Bean> implements
        SingleObjectCache<E> {

    @NotNull
    private RedisTemplate<String, JE> template;
    @NotNull
    private StringKeyFormatter<K> formatter;
    @NotNull
    private BeanTransformer<E, JE> transformer;
    @NotNull
    private K key;

    public RedisSingleObjectCache(
            @NotNull RedisTemplate<String, JE> template,
            @NotNull StringKeyFormatter<K> formatter,
            @NotNull BeanTransformer<E, JE> transformer,
            @NotNull K key
    ) {
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
        // 获得装箱后的结果，拆箱并返回。
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

    @NotNull
    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull RedisTemplate<String, JE> template) {
        this.template = template;
    }

    @NotNull
    public StringKeyFormatter<K> getFormatter() {
        return formatter;
    }

    public void setFormatter(@NotNull StringKeyFormatter<K> formatter) {
        this.formatter = formatter;
    }

    @NotNull
    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NotNull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }

    @NotNull
    public K getKey() {
        return key;
    }

    public void setKey(@NotNull K key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "RedisSingleObjectCache{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                ", key=" + key +
                '}';
    }
}
