package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

/**
 * 通过 Redis 实现的单对象数据访问层。
 *
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisSingleObjectDao<K extends Key, E extends Entity<K>, JE extends Bean> implements SingleObjectDao<E> {

    private RedisTemplate<String, JE> template;
    private StringKeyFormatter<K> formatter;
    private BeanTransformer<E, JE> transformer;
    private K key;

    public RedisSingleObjectDao(
            @NonNull RedisTemplate<String, JE> template,
            @NonNull StringKeyFormatter<K> formatter,
            @NonNull BeanTransformer<E, JE> transformer,
            @NonNull K key
    ) {
        this.template = template;
        this.formatter = formatter;
        this.transformer = transformer;
        this.key = key;
    }

    @Override
    public boolean exists() throws DaoException {
        try {
            return internalExists();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalExists() {
        return template.hasKey(formatter.format(key));
    }

    @Override
    public E get() throws DaoException {
        try {
            return transformer.reverseTransform(template.opsForValue().get(formatter.format(key)));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void put(E entity) throws DaoException {
        try {
            @SuppressWarnings("unchecked")
            E newEntity = (E) BeanUtils.cloneBean(entity);
            newEntity.setKey(key);
            template.opsForValue().set(formatter.format(key), transformer.transform(newEntity));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void clear() throws DaoException {
        try {
            template.delete(formatter.format(key));
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

    public K getKey() {
        return key;
    }

    public void setKey(@NonNull K key) {
        this.key = key;
    }
}
