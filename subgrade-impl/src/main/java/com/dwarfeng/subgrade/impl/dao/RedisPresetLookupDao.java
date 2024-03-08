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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的 PresetLookupDao。
 *
 * <p>
 * 由于数据存放在内存中，请不要使用该数据访问层存储大量的数据。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisPresetLookupDao<K extends Key, E extends Entity<K>, JE extends Bean> implements PresetLookupDao<E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;
    @Nonnull
    private PresetEntityFilter<E> filter;
    @Nonnull
    private String dbKey;

    public RedisPresetLookupDao(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer,
            @Nonnull PresetEntityFilter<E> filter,
            @Nonnull String dbKey
    ) {
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
            // 展开参数。
            int page = pagingInfo.getPage();
            int rows = pagingInfo.getRows();
            // 每页行数大于 0 时，按照正常的逻辑查询数据。
            if (rows > 0) {
                return lookupWithPositiveValue(preset, objs, page, rows);
            }
            // 否则返回空列表。
            else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Nonnull
    private List<E> lookupWithPositiveValue(String preset, Object[] objs, int page, int rows) {
        int beginIndex = page * rows;
        int endIndex = beginIndex + rows;
        List<E> es = internalEntireLookup(preset, objs);
        return es.subList(beginIndex, Math.min(es.size(), endIndex));
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

    @Nonnull
    public PresetEntityFilter<E> getFilter() {
        return filter;
    }

    public void setFilter(@Nonnull PresetEntityFilter<E> filter) {
        this.filter = filter;
    }

    @Nonnull
    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@Nonnull String dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    public String toString() {
        return "RedisPresetLookupDao{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                ", filter=" + filter +
                ", dbKey='" + dbKey + '\'' +
                '}';
    }
}
