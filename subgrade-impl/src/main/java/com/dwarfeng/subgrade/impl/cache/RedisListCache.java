package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.ListCache;
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
public class RedisListCache<E extends Entity<?>, JE extends Bean> implements ListCache<E> {

    private String key;
    private RedisTemplate<String, JE> template;
    private BeanTransformer<E, JE> transformer;

    public RedisListCache(
            @NonNull String key,
            @NonNull RedisTemplate<String, JE> template,
            @NonNull BeanTransformer<E, JE> transformer) {
        this.key = key;
        this.template = template;
        this.transformer = transformer;
    }

    @Override
    public boolean exists() throws CacheException {
        try {
            return template.hasKey(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public int size() throws CacheException {
        try {
            return template.opsForList().size(key).intValue();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public List<E> get() throws CacheException {
        try {
            Long totalSize = template.opsForList().size(key);
            if (totalSize == 0) {
                return new ArrayList<>();
            } else {
                List<JE> range = template.opsForList().range(key, 0, totalSize - 1);
                return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public List<E> get(int beginIndex, int maxEntity) throws CacheException {
        try {
            List<JE> range = template.opsForList().range(key, beginIndex, beginIndex + maxEntity - 1);
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public List<E> get(PagingInfo pagingInfo) throws CacheException {
        try {
            Long totalSize = template.opsForList().size(key);
            long beginIndex = pagingInfo.getRows() * pagingInfo.getPage();
            long endIndex = Math.min(totalSize, beginIndex + pagingInfo.getRows()) - 1;
            List<JE> range = template.opsForList().range(key, beginIndex, endIndex);
            return range.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void set(Collection<E> entities, long timeout) throws CacheException {
        try {
            template.delete(key);
            if (entities.isEmpty()) {
                return;
            }
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().rightPushAll(key, collect);
            template.expire(key, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void leftPush(Collection<E> entities, long timeout) throws CacheException {
        try {
            if (entities.isEmpty()) {
                return;
            }
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().leftPushAll(key, collect);
            template.expire(key, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void rightPush(Collection<E> entities, long timeout) throws CacheException {
        try {
            if (entities.isEmpty()) {
                return;
            }
            List<JE> collect = entities.stream().map(transformer::transform).collect(Collectors.toList());
            template.opsForList().rightPushAll(key, collect);
            template.expire(key, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            template.delete(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull RedisTemplate<String, JE> template) {
        this.template = template;
    }

    public BeanTransformer<E, JE> getTransformer() {
        return transformer;
    }

    public void setTransformer(@NonNull BeanTransformer<E, JE> transformer) {
        this.transformer = transformer;
    }
}
