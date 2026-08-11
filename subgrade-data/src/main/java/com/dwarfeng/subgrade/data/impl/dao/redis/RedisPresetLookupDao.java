package com.dwarfeng.subgrade.data.impl.dao.redis;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.sdk.memory.filter.PresetEntityFilter;
import com.dwarfeng.subgrade.data.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.data.stack.dao.PresetLookupDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

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

    @NotNull
    private RedisTemplate<String, JE> template;
    @NotNull
    private StringKeyFormatter<K> formatter;
    @NotNull
    private BeanTransformer<E, JE> transformer;
    @NotNull
    private PresetEntityFilter<E> filter;
    @NotNull
    private String dbKey;

    public RedisPresetLookupDao(
            @NotNull RedisTemplate<String, JE> template,
            @NotNull StringKeyFormatter<K> formatter,
            @NotNull BeanTransformer<E, JE> transformer,
            @NotNull PresetEntityFilter<E> filter,
            @NotNull String dbKey
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

    @NotNull
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
        List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).toList();
        List<E> es = jes.stream().map(transformer::reverseTransform).toList();
        return es.stream().filter(e -> filter.accept(e, preset, objs)).collect(Collectors.toList());
    }

    /**
     * @since 1.2.8
     */
    @Override
    public E lookupFirst(String preset, Object[] objs) throws DaoException {
        try {
            @SuppressWarnings("unchecked")
            List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).toList();
            List<E> es = jes.stream().map(transformer::reverseTransform).toList();
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
    public PresetEntityFilter<E> getFilter() {
        return filter;
    }

    public void setFilter(@NotNull PresetEntityFilter<E> filter) {
        this.filter = filter;
    }

    @NotNull
    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@NotNull String dbKey) {
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
