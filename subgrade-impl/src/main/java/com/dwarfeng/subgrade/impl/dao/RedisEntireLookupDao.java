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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的 EntireLookupDao。
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
public class RedisEntireLookupDao<K extends Key, E extends Entity<K>, JE extends Bean> implements EntireLookupDao<E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;
    @Nonnull
    private String dbKey;

    public RedisEntireLookupDao(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer,
            @Nonnull String dbKey
    ) {
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
        @SuppressWarnings("unchecked")
        List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).collect(Collectors.toList());
        return jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            int beginIndex = pagingInfo.getPage() * pagingInfo.getRows();
            int endIndex = beginIndex + pagingInfo.getRows();
            List<E> es = internalLookup();
            return es.subList(beginIndex, Math.min(es.size(), endIndex));
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

    /**
     * @since 1.2.8
     */
    @Override
    public E lookupFirst() throws DaoException {
        try {
            @SuppressWarnings("unchecked") E result = template.opsForHash().values(dbKey).stream().findFirst()
                    .map(o -> transformer.reverseTransform((JE) o)).orElse(null);
            return result;
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
    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@Nonnull String dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    public String toString() {
        return "RedisEntireLookupDao{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                ", dbKey='" + dbKey + '\'' +
                '}';
    }
}
