package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.impl.cache.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BaseCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 实现的 BaseCache。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class RedisBaseCache<K extends Key, E extends Entity<K>, JE extends Bean> implements BaseCache<K, E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;

    public RedisBaseCache(
            @Nullable RedisTemplate<String, JE> template,
            @Nullable StringKeyFormatter<K> formatter,
            @Nullable BeanTransformer<E, JE> transformer) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
    }

    @Override
    public boolean exists(K key) throws CacheException {
        try {
            return template.hasKey(formatKey(key));
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public E get(K key) throws CacheException {
        try {
            JE je = template.opsForValue().get(formatKey(key));
            return transformer.reverseTransform(je);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void push(K key, E value, long timeout) throws CacheException {
        try {
            template.opsForValue().set(
                    formatKey(key),
                    transformer.transform(value),
                    timeout, TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void delete(K key) throws CacheException {
        try {
            template.delete(formatKey(key));
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            template.delete(template.keys(formatter.generalFormat()));
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    private String formatKey(K key) {
        return formatter.format(key);
    }

    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@Nullable RedisTemplate<String, JE> template) {
        this.template = template;
    }

    public StringKeyFormatter<K> getFormatter() {
        return formatter;
    }

    public void setFormatter(@Nullable StringKeyFormatter<K> formatter) {
        this.formatter = formatter;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@Nullable BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }
}
