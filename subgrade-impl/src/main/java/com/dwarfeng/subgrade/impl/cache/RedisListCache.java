package com.dwarfeng.subgrade.impl.cache;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.cache.ListCache;
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
public class RedisListCache<E extends Entity<?>, JE extends Bean> implements ListCache<E> {

    @Nonnull
    private String key;
    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private BeanTransformer<E, JE> transformer;

    public RedisListCache(
            @Nonnull String key,
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull BeanTransformer<E, JE> transformer
    ) {
        this.key = key;
        this.template = template;
        this.transformer = transformer;
    }

    @Override
    public boolean exists() throws CacheException {
        try {
            // 获得装箱后的结果，拆箱并返回。
            return template.hasKey(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public int size() throws CacheException {
        try {
            // 获得装箱后的结果。
            Long result = template.opsForList().size(key);
            // 拆箱并返回。
            return result == null ? 0 : result.intValue();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<E> get() throws CacheException {
        try {
            Long totalSize = template.opsForList().size(key);
            if (Objects.isNull(totalSize) || totalSize == 0) {
                return new ArrayList<>();
            } else {
                List<JE> range = template.opsForList().range(key, 0, totalSize - 1);
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
    public List<E> get(int beginIndex, int maxEntity) throws CacheException {
        try {
            List<JE> range = template.opsForList().range(key, beginIndex, beginIndex + maxEntity - 1);
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
    public List<E> get(PagingInfo pagingInfo) throws CacheException {
        try {
            Long totalSize = template.opsForList().size(key);
            if (Objects.isNull(totalSize) || totalSize == 0) {
                return Collections.emptyList();
            }

            int page = pagingInfo.getPage();
            int rows = pagingInfo.getRows();

            List<JE> jes;
            // 每页行数大于 0 时，按照正常的逻辑查询数据。
            if (rows > 0) {
                long beginIndex = (long) rows * page;
                long endIndex = Math.min(totalSize, beginIndex + rows) - 1;
                jes = template.opsForList().range(key, beginIndex, endIndex);
            }
            // 否则返回空列表。
            else {
                jes = Collections.emptyList();
            }

            if (Objects.isNull(jes)) {
                return Collections.emptyList();
            }
            return jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
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

    @Nonnull
    public String getKey() {
        return key;
    }

    public void setKey(@Nonnull String key) {
        this.key = key;
    }

    @Nonnull
    public RedisTemplate<String, JE> getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull RedisTemplate<String, JE> template) {
        this.template = template;
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
        return "RedisListCache{" +
                "key='" + key + '\'' +
                ", template=" + template +
                ", transformer=" + transformer +
                '}';
    }
}
