package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.cache.KeyListCache;
import com.dwarfeng.subgrade.stack.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 使用 Redis 实现的 ListCache。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisKeyListCache<K extends Key, E extends Entity<? extends Key>, JE extends Bean> implements KeyListCache<K, E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;

    public RedisKeyListCache(
            @NonNull RedisTemplate<String, JE> template,
            @NonNull StringKeyFormatter<K> formatter,
            @NonNull BeanTransformer<E, JE> transformer) {
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
    public int size(K key) throws CacheException {
        try {
            String formatKey = formatKey(key);
            return template.opsForList().size(formatKey).intValue();
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public List<E> get(K key) throws CacheException {
        try {
            String formatKey = formatKey(key);
            Long totleSize = template.opsForList().size(formatKey);
            if (totleSize == 0) {
                return new ArrayList<>();
            } else {
                List<JE> range = template.opsForList().range(formatKey, 0, totleSize - 1);
                return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public List<E> get(K key, int beginIndex, int maxEntity) throws CacheException {
        try {
            String formatKey = formatKey(key);
            List<JE> range = template.opsForList().range(formatKey, beginIndex, beginIndex + maxEntity - 1);
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public List<E> get(K key, PagingInfo pagingInfo) throws CacheException {
        try {
            String formatKey = formatKey(key);
            Long totleSize = template.opsForList().size(formatKey);
            long beginIndex = pagingInfo.getRows() * pagingInfo.getPage();
            long endIndex = Math.min(totleSize, beginIndex + pagingInfo.getRows()) - 1;
            List<JE> range = template.opsForList().range(formatKey, beginIndex, endIndex);
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void set(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            String formatKey = formatKey(key);
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.delete(formatKey);
            template.opsForList().leftPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void leftPush(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            String formatKey = formatKey(key);
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().leftPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException();
        }
    }

    @Override
    public void rightPush(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            String formatKey = formatKey(key);
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().rightPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
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

    public void setTemplate(@NonNull RedisTemplate<String, JE> template) {
        this.template = template;
    }

    public StringKeyFormatter<K> getFormatter() {
        return formatter;
    }

    public void setFormatter(@NonNull StringKeyFormatter<K> formatter) {
        this.formatter = formatter;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NonNull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }
}
