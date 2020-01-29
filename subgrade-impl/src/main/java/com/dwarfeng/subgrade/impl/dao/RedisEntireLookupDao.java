package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的 EntireLookupDao。
 * <p>由于数据存放在内存中，请不要使用该数据访问层存储大量的数据。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisEntireLookupDao<K extends Key, E extends Entity<K>, JE extends Bean> implements EntireLookupDao<E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;
    private String dbKey;

    public RedisEntireLookupDao(
            @NonNull RedisTemplate<String, JE> template,
            @NonNull StringKeyFormatter<K> formatter,
            @NonNull BeanTransformer<E, JE> transformer,
            @NonNull String dbKey) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
        this.dbKey = dbKey;
    }

    @Override
    public List<E> lookup() throws DaoException {
        try {
            return internalLookup();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private List<E> internalLookup() {
        String s = formatter.generalFormat();
        Set<String> keys = template.keys(s);
        List<JE> jes = new ArrayList<>();
        for (String key : keys) {
            //noinspection unchecked
            jes.add((JE) template.opsForHash().get(dbKey, key));
        }
        return jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            int beginIndex = pagingInfo.getPage() * pagingInfo.getRows();
            int endIndex = beginIndex + pagingInfo.getRows() - 1;
            List<E> es = internalLookup();
            return es.subList(beginIndex, endIndex);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int lookupCount() throws DaoException {
        try {
            return template.opsForHash().size(dbKey).intValue();
        } catch (Exception e) {
            throw new DaoException(e);
        }
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

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@NonNull String dbKey) {
        this.dbKey = dbKey;
    }
}