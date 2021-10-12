package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的基础数据访问层。
 * <p>由于数据存放在内存中，请不要使用该数据访问层存储大量的数据。</p>
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisBatchBaseDao<K extends Key, E extends Entity<K>, JE extends Bean> implements BatchBaseDao<K, E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;
    private String dbKey;

    public RedisBatchBaseDao(
            @NonNull RedisTemplate<String, JE> template, @NonNull StringKeyFormatter<K> formatter,
            @NonNull BeanTransformer<E, JE> transformer, @NotNull String dbKey
    ) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
        this.dbKey = dbKey;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public K insert(E element) throws DaoException {
        try {
            if (internalExists(element.getKey())) {
                throw new DaoException("元素已经存在。");
            }
            template.opsForHash().put(dbKey, formatter.format(element.getKey()), transformer.transform(element));
            return element.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E element) throws DaoException {
        try {
            if (!internalExists(element.getKey())) {
                throw new DaoException("元素不存在。");
            }
            template.opsForHash().put(dbKey, formatter.format(element.getKey()), transformer.transform(element));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            if (!internalExists(key)) {
                throw new DaoException("元素不存在。");
            }
            String format = formatter.format(key);
            template.opsForHash().delete(dbKey, format);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exists(K key) throws DaoException {
        try {
            return internalExists(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalExists(K key) {
        String format = formatter.format(key);
        return template.opsForHash().hasKey(dbKey, format);
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            String format = formatter.format(key);
            @SuppressWarnings("unchecked")
            JE je = (JE) template.opsForHash().get(dbKey, format);
            return transformer.reverseTransform(je);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public List<K> batchInsert(List<E> elements) throws DaoException {
        try {
            List<K> keys = elements.stream().map(E::getKey).collect(Collectors.toList());
            if (!internalNonExists(keys)) {
                throw new DaoException("至少一个元素的主键已经存在");
            }
            List<String> formats = keys.stream().map(formatter::format).collect(Collectors.toList());
            for (int i = 0; i < elements.size(); i++) {
                String format = formats.get(i);
                E element = elements.get(i);
                template.opsForHash().put(dbKey, format, transformer.transform(element));
            }
            return keys;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void batchUpdate(List<E> elements) throws DaoException {
        try {
            List<K> keys = elements.stream().map(E::getKey).collect(Collectors.toList());
            if (!internalAllExists(keys)) {
                throw new DaoException("至少一个元素的主键不存在");
            }
            List<String> formats = keys.stream().map(formatter::format).collect(Collectors.toList());
            for (int i = 0; i < elements.size(); i++) {
                String format = formats.get(i);
                E element = elements.get(i);
                template.opsForHash().put(dbKey, format, transformer.transform(element));
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            if (!internalAllExists(keys)) {
                throw new DaoException("至少一个元素的主键不存在");
            }
            Object[] formats = keys.stream().map(formatter::format).toArray();
            if (formats.length > 0) {
                template.opsForHash().delete(dbKey, formats);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean allExists(List<K> keys) throws DaoException {
        try {
            return internalAllExists(keys);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalAllExists(List<K> keys) {
        for (K key : keys) {
            if (!internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public boolean nonExists(List<K> keys) throws DaoException {
        try {
            return internalNonExists(keys);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalNonExists(List<K> keys) {
        for (K key : keys) {
            if (internalExists(key)) return false;
        }
        return true;
    }

    @Override
    public List<E> batchGet(List<K> keys) throws DaoException {
        try {
            List<String> formats = keys.stream().map(formatter::format).collect(Collectors.toList());
            List<JE> jes = new ArrayList<>();
            for (String format : formats) {
                @SuppressWarnings("unchecked")
                JE je = (JE) template.opsForHash().get(dbKey, format);
                jes.add(je);
            }
            return jes.stream().map(transformer::reverseTransform).collect(Collectors.toList());
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

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }
}
