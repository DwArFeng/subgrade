package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.WriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Nonnull;

/**
 * 使用 MyBatis 实现的 WriteDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisWriteDao<E extends Entity<?>> implements WriteDao<E> {

    public static final String DEFAULT_WRITE_ID = "write";

    private SqlSessionTemplate template;
    private String namespace;
    private String writeId;

    public MybatisWriteDao(@Nonnull SqlSessionTemplate template, @Nonnull String namespace) {
        this(template, namespace, DEFAULT_WRITE_ID);
    }

    public MybatisWriteDao(
            @Nonnull SqlSessionTemplate template,
            @Nonnull String namespace,
            @Nonnull String writeId) {
        this.template = template;
        this.namespace = namespace;
        this.writeId = writeId;
    }

    @Override
    public void write(E element) throws DaoException {
        try {
            template.insert(concatId(writeId), element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private String concatId(String id) {
        return namespace + '.' + id;
    }

    public SqlSessionTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SqlSessionTemplate template) {
        this.template = template;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getWriteId() {
        return writeId;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }
}
