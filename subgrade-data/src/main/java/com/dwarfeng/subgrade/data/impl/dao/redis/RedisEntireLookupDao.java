package com.dwarfeng.subgrade.data.impl.dao.redis;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.data.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
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

    @NotNull
    private RedisTemplate<String, JE> template;
    @NotNull
    private StringKeyFormatter<K> formatter;
    @NotNull
    private BeanTransformer<E, JE> transformer;
    @NotNull
    private String dbKey;

    public RedisEntireLookupDao(
            @NotNull RedisTemplate<String, JE> template,
            @NotNull StringKeyFormatter<K> formatter,
            @NotNull BeanTransformer<E, JE> transformer,
            @NotNull String dbKey
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
        List<JE> jes = template.opsForHash().values(dbKey).stream().map(o -> (JE) o).toList();
        return jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) throws DaoException {
        try {
            // 展开参数。
            int page = pagingInfo.getPage();
            int rows = pagingInfo.getRows();
            // 每页行数大于 0 时，按照正常的逻辑查询数据。
            if (rows > 0) {
                return lookupWithPositiveRows(page, rows);
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
    private List<E> lookupWithPositiveRows(int page, int rows) {
        int beginIndex = page * rows;
        int endIndex = beginIndex + rows;
        List<E> es = internalLookup();
        return es.subList(beginIndex, Math.min(es.size(), endIndex));
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
    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(@NotNull String dbKey) {
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
