package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Nonnull;

/**
 * 使用 MyBatis 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisBaseDao<K extends Key, E extends Entity<K>> implements BaseDao<K, E> {

    public static final String DEFAULT_INSERT_ID = "insert";
    public static final String DEFAULT_UPDATE_ID = "update";
    public static final String DEFAULT_DELETE_ID = "delete";
    public static final String DEFAULT_EXISTS_ID = "exists";
    public static final String DEFAULT_GET_ID = "get";

    private SqlSessionTemplate template;
    private String namespace;
    private String insertId;
    private String updateId;
    private String deleteId;
    private String existsId;
    private String getId;

    public MybatisBaseDao(@Nonnull SqlSessionTemplate template, @Nonnull String namespace) {
        this(template, namespace, DEFAULT_INSERT_ID, DEFAULT_UPDATE_ID, DEFAULT_DELETE_ID, DEFAULT_EXISTS_ID,
                DEFAULT_GET_ID);
    }

    public MybatisBaseDao(
            @Nonnull SqlSessionTemplate template,
            @Nonnull String namespace,
            @Nonnull String insertId,
            @Nonnull String updateId,
            @Nonnull String deleteId,
            @Nonnull String existsId,
            @Nonnull String getId) {
        this.template = template;
        this.namespace = namespace;
        this.insertId = insertId;
        this.updateId = updateId;
        this.deleteId = deleteId;
        this.existsId = existsId;
        this.getId = getId;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            template.insert(concatId(insertId), element);
            return element.getKey();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E element) throws DaoException {
        try {
            template.update(concatId(insertId), element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            template.delete(concatId(insertId), key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exists(K key) throws DaoException {
        try {
            return template.selectOne(concatId(existsId), key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            return template.selectOne(concatId(getId), key);
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

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getExistsId() {
        return existsId;
    }

    public void setExistsId(String existsId) {
        this.existsId = existsId;
    }

    public String getGetId() {
        return getId;
    }

    public void setGetId(String getId) {
        this.getId = getId;
    }
}
