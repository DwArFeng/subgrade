package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.memory.filter.PresetEntityFilter;
import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的 PresetLookupDao。
 * <p>由于数据存放在内存中，请不要使用该数据访问层存储大量的数据。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisPresetLookupDao<K extends Key, E extends Entity<K>, JE extends Bean> implements PresetLookupDao<E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;
    private PresetEntityFilter<E> filter;
    private String dbKey;

    public RedisPresetLookupDao(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer,
            @Nonnull PresetEntityFilter<E> filter,
            @Nonnull String dbKey) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
        this.filter = filter;
        this.dbKey = dbKey;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) throws DaoException {
        try {
            return internalEntireLookup(preset, objs);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        try {
            int beginIndex = pagingInfo.getPage() * pagingInfo.getRows();
            int endIndex = beginIndex + pagingInfo.getRows();
            List<E> es = internalEntireLookup(preset, objs);
            return es.subList(beginIndex, Math.min(es.size(), endIndex));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        try {
            return internalEntireLookup(preset, objs).size();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private List<E> internalEntireLookup(String preset, Object[] objs) {
        @SuppressWarnings("unchecked")
        List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).collect(Collectors.toList());
        List<E> es = jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
        return es.stream().filter(e -> filter.accept(e, preset, objs)).collect(Collectors.toList());
    }

    /**
     * @since 1.2.8
     */
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
            @SuppressWarnings("unchecked")
            List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).collect(Collectors.toList());
            List<E> es = jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
            for (E e : es) {
                if (filter.accept(e, preset, objs)) {
                    return e;
                }
            }
            return null;
        } catch (Exception e) {
            throw new DaoException(e);
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

    public PresetEntityFilter<E> getFilter() {
        return filter;
    }

    public void setFilter(@Nonnull PresetEntityFilter<E> filter) {
        this.filter = filter;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@Nonnull String dbKey) {
        this.dbKey = dbKey;
    }
}
