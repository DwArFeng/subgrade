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

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 使用 Redis 实现的 ListCache。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisKeyListCache<K extends Key, E extends Entity<? extends Key>, JE extends Bean> implements
        KeyListCache<K, E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;

    public RedisKeyListCache(
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
            // 获得装箱后的结果。
            Boolean result = template.hasKey(formatKey(key));
            // 拆箱并返回。
            return result != null && result;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public int size(K key) throws CacheException {
        try {
            String formatKey = formatKey(key);
            // 获得装箱后的结果。
            Long result = template.opsForList().size(formatKey);
            // 拆箱并返回。
            return result == null ? 0 : result.intValue();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> get(K key) throws CacheException {
        try {
            String formatKey = formatKey(key);
            Long totalSize = template.opsForList().size(formatKey);
            if (Objects.isNull(totalSize) || totalSize == 0) {
                return new ArrayList<>();
            } else {
                List<JE> range = template.opsForList().range(formatKey, 0, totalSize - 1);
                if (Objects.isNull(range)) {
                    return new ArrayList<>();
                }
                return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> get(K key, int beginIndex, int maxEntity) throws CacheException {
        try {
            String formatKey = formatKey(key);
            List<JE> range = template.opsForList().range(formatKey, beginIndex, beginIndex + maxEntity - 1);
            if (Objects.isNull(range)) {
                return new ArrayList<>();
            }
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> get(K key, PagingInfo pagingInfo) throws CacheException {
        try {
            String formatKey = formatKey(key);
            Long totalSize = template.opsForList().size(formatKey);
            if (Objects.isNull(totalSize) || totalSize == 0) {
                return new ArrayList<>();
            }
            long beginIndex = (long) pagingInfo.getRows() * pagingInfo.getPage();
            long endIndex = Math.min(totalSize, beginIndex + pagingInfo.getRows()) - 1;
            List<JE> range = template.opsForList().range(formatKey, beginIndex, endIndex);
            if (Objects.isNull(range)) {
                return new ArrayList<>();
            }
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void set(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            String formatKey = formatKey(key);
            template.delete(formatKey);
            if (entities.isEmpty()) {
                return;
            }
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().leftPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void leftPush(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            if (entities.isEmpty()) {
                return;
            }
            String formatKey = formatKey(key);
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().leftPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void rightPush(K key, Collection<E> entities, long timeout) throws CacheException {
        try {
            if (entities.isEmpty()) {
                return;
            }
            String formatKey = formatKey(key);
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().rightPushAll(formatKey, collect);
            template.expire(formatKey, timeout, TimeUnit.MILLISECONDS);
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
        return "RedisKeyListCache{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                '}';
    }
}
