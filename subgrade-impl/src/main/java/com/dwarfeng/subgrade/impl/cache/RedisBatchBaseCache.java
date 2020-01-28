package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.impl.cache.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.BatchBaseCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 实现的 BaseCache。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class RedisBatchBaseCache<K extends Key, E extends Entity<K>, JE extends Bean> implements BatchBaseCache<K, E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;

    public RedisBatchBaseCache(
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
            return internalExists(key);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public E get(K key) throws CacheException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void push(K key, E value, long timeout) throws CacheException {
        try {
            internalPush(key, value, timeout);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void delete(K key) throws CacheException {
        try {
            internalDelete(key);
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

    private boolean internalExists(K key) {
        return template.hasKey(formatKey(key));
    }

    private E internalGet(K key) {
        JE je = template.opsForValue().get(formatKey(key));
        return transformer.reverseTransform(je);
    }

    private void internalPush(K key, E value, long timeout) {
        template.opsForValue().set(
                formatKey(key),
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
            throw new CacheException();
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
            throw new CacheException();
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
            throw new CacheException();
        }
    }

    @Override
    public void batchPush(List<K> keys, List<E> entities, long timeout) throws CacheException {
        try {
            if (keys.size() != entities.size()) {
                throw new CacheException("keys 的数量与 entities 的数量不相等，keys的数量为 "
                        + keys.size() + " ，entities 的数量为 " + entities.size());
            }
            for (int i = 0; i < keys.size(); i++) {
                K key = keys.get(i);
                E entity = entities.get(i);
                internalPush(key, entity, timeout);
            }
        } catch (CacheException e) {
            throw e;
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws CacheException {
        try {
            for (K key : keys) {
                internalDelete(key);
            }
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
