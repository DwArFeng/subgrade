package com.dwarfeng.subgrade.data.impl.dao.redis;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.data.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过 Redis 实现的基础数据访问层。
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
public class RedisBatchBaseDao<K extends Key, E extends Entity<K>, JE extends Bean> implements BatchBaseDao<K, E> {

    @NotNull
    private RedisTemplate<String, JE> template;
    @NotNull
    private StringKeyFormatter<K> formatter;
    @NotNull
    private BeanTransformer<E, JE> transformer;
    @NotNull
    private String dbKey;

    public RedisBatchBaseDao(
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
            List<String> formats = keys.stream().map(formatter::format).toList();
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
            List<String> formats = keys.stream().map(formatter::format).toList();
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
            List<String> formats = keys.stream().map(formatter::format).toList();
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
        return "RedisBatchBaseDao{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                ", dbKey='" + dbKey + '\'' +
                '}';
    }
}
