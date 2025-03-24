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

import javax.annotation.Nonnull;

/**
 * 通过 Redis 实现的单对象数据访问层。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class RedisSingleObjectDao<K extends Key, E extends Entity<K>, JE extends Bean> implements SingleObjectDao<E> {

    @Nonnull
    private RedisTemplate<String, JE> template;
    @Nonnull
    private StringKeyFormatter<K> formatter;
    @Nonnull
    private BeanTransformer<E, JE> transformer;
    @Nonnull
    private K key;

    public RedisSingleObjectDao(
            @Nonnull RedisTemplate<String, JE> template,
            @Nonnull StringKeyFormatter<K> formatter,
            @Nonnull BeanTransformer<E, JE> transformer,
            @Nonnull K key
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
        // 获得装箱后的结果，拆箱并返回。
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
    public K getKey() {
        return key;
    }

    public void setKey(@Nonnull K key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "RedisSingleObjectDao{" +
                "template=" + template +
                ", formatter=" + formatter +
                ", transformer=" + transformer +
                ", key=" + key +
                '}';
    }
}
