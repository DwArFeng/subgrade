package com.dwarfeng.subgrade.data.impl.dao.redis;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.internal.bean.BeanPropertyAccess;
import com.dwarfeng.subgrade.data.sdk.redis.formatter.StringKeyFormatter;
import com.dwarfeng.subgrade.data.stack.dao.SingleObjectDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

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

    @NotNull
    private RedisTemplate<String, JE> template;
    @NotNull
    private StringKeyFormatter<K> formatter;
    @NotNull
    private BeanTransformer<E, JE> transformer;
    @NotNull
    private K key;

    public RedisSingleObjectDao(
            @NotNull RedisTemplate<String, JE> template,
            @NotNull StringKeyFormatter<K> formatter,
            @NotNull BeanTransformer<E, JE> transformer,
            @NotNull K key
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
            E newEntity = BeanPropertyAccess.getInstance().cloneBean(entity);
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
    public K getKey() {
        return key;
    }

    public void setKey(@NotNull K key) {
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
