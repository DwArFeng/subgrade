package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchBaseDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 MyBatis 实现的 BaseDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisBatchBaseDao<K extends Key, E extends Entity<K>> implements BatchBaseDao<K, E> {

    public static final String DEFAULT_INSERT_ID = "insert";
    public static final String DEFAULT_UPDATE_ID = "update";
    public static final String DEFAULT_DELETE_ID = "delete";
    public static final String DEFAULT_EXIST_ID = "exist";
    public static final String DEFAULT_GET_ID = "get";
    public static final String DEFAULT_BATCH_INSERT_ID = "batchInsert";
    public static final String DEFAULT_BATCH_UPDATE_ID = "batchUpdate";
    public static final String DEFAULT_BATCH_DELETE_ID = "batchDelete";
    public static final String DEFAULT_NON_EXISTS_ID = "nonExists";
    public static final String DEFAULT_ALL_EXISTS_ID = "allExists";
    public static final String DEFAULT_BATCH_GET_ID = "get";

    private SqlSessionTemplate template;
    private String namespace;
    private String insertId;
    private String updateId;
    private String deleteId;
    private String existsId;
    private String getId;
    private String batchInsertId;
    private String batchUpdateId;
    private String batchDeleteId;
    private String nonExistsId;
    private String allExistsId;
    private String batchGetId;

    public MybatisBatchBaseDao(
            @Nonnull SqlSessionTemplate template, @Nonnull String namespace, boolean defaultBatchOperation) {
        this(
                template, namespace, DEFAULT_INSERT_ID, DEFAULT_UPDATE_ID,
                DEFAULT_DELETE_ID, DEFAULT_EXIST_ID, DEFAULT_GET_ID,
                defaultBatchOperation ? DEFAULT_INSERT_ID : DEFAULT_BATCH_INSERT_ID,
                defaultBatchOperation ? DEFAULT_UPDATE_ID : DEFAULT_BATCH_UPDATE_ID,
                defaultBatchOperation ? DEFAULT_DELETE_ID : DEFAULT_BATCH_DELETE_ID,
                defaultBatchOperation ? DEFAULT_EXIST_ID : DEFAULT_NON_EXISTS_ID,
                defaultBatchOperation ? DEFAULT_EXIST_ID : DEFAULT_ALL_EXISTS_ID,
                defaultBatchOperation ? DEFAULT_GET_ID : DEFAULT_BATCH_GET_ID
        );
    }

    public MybatisBatchBaseDao(
            @Nonnull SqlSessionTemplate template, @Nonnull String namespace, @Nonnull String insertId,
            @Nonnull String updateId, @Nonnull String deleteId, @Nonnull String existsId,
            @Nonnull String getId, @Nonnull String batchInsertId, @Nonnull String batchUpdateId,
            @Nonnull String batchDeleteId, @Nonnull String nonExistsId, @Nonnull String allExistsId,
            @Nonnull String batchGetId) {
        this.template = template;
        this.namespace = namespace;
        this.insertId = insertId;
        this.updateId = updateId;
        this.deleteId = deleteId;
        this.existsId = existsId;
        this.getId = getId;
        this.batchInsertId = batchInsertId;
        this.batchUpdateId = batchUpdateId;
        this.batchDeleteId = batchDeleteId;
        this.nonExistsId = nonExistsId;
        this.allExistsId = allExistsId;
        this.batchGetId = batchGetId;
    }

    @Override
    public K insert(E element) throws DaoException {
        try {
            return internalInsert(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private K internalInsert(E element) {
        template.insert(concatId(insertId), element);
        return element.getKey();
    }

    @Override
    public void update(E element) throws DaoException {
        try {
            internalUpdate(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalUpdate(E element) {
        template.update(concatId(insertId), element);
    }

    @Override
    public void delete(K key) throws DaoException {
        try {
            internalDelete(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalDelete(K key) {
        template.delete(concatId(insertId), key);
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
        return template.selectOne(concatId(existsId), key);
    }

    @Override
    public E get(K key) throws DaoException {
        try {
            return internalGet(key);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private E internalGet(K key) {
        return template.selectOne(concatId(getId), key);
    }

    @Override
    public List<K> batchInsert(List<E> elements) throws DaoException {
        try {
            if (Objects.equals(insertId, batchInsertId)) {
                List<K> keys = new ArrayList<>();
                for (E element : elements) {
                    keys.add(internalInsert(element));
                }
                return keys;
            }
            template.insert(concatId(batchInsertId), elements);
            return elements.stream().map(Entity::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchUpdate(List<E> elements) throws DaoException {
        try {
            if (Objects.equals(updateId, batchUpdateId)) {
                for (E element : elements) {
                    internalUpdate(element);
                }
            }
            template.update(concatId(batchUpdateId), elements);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDelete(List<K> keys) throws DaoException {
        try {
            if (Objects.equals(deleteId, batchDeleteId)) {
                for (K key : keys) {
                    internalDelete(key);
                }
            }
            template.delete(concatId(batchDeleteId), keys);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean allExists(List<K> keys) throws DaoException {
        try {
            if (Objects.equals(existsId, allExistsId)) {
                for (K key : keys) {
                    if (!internalExists(key)) return false;
                }
                return true;
            }
            return template.selectOne(concatId(allExistsId), keys);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean nonExists(List<K> keys) throws DaoException {
        try {
            if (Objects.equals(existsId, nonExistsId)) {
                for (K key : keys) {
                    if (internalExists(key)) return false;
                }
                return true;
            }
            return template.selectOne(concatId(nonExistsId), keys);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> batchGet(List<K> keys) throws DaoException {
        try {
            if (Objects.equals(getId, batchGetId)) {
                List<E> elements = new ArrayList<>();
                for (K key : keys) {
                    elements.add(internalGet(key));
                }
                return elements;
            }
            return template.selectList(concatId(nonExistsId), keys);
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

    public String getBatchInsertId() {
        return batchInsertId;
    }

    public void setBatchInsertId(String batchInsertId) {
        this.batchInsertId = batchInsertId;
    }

    public String getBatchUpdateId() {
        return batchUpdateId;
    }

    public void setBatchUpdateId(String batchUpdateId) {
        this.batchUpdateId = batchUpdateId;
    }

    public String getBatchDeleteId() {
        return batchDeleteId;
    }

    public void setBatchDeleteId(String batchDeleteId) {
        this.batchDeleteId = batchDeleteId;
    }

    public String getNonExistsId() {
        return nonExistsId;
    }

    public void setNonExistsId(String nonExistsId) {
        this.nonExistsId = nonExistsId;
    }

    public String getAllExistsId() {
        return allExistsId;
    }

    public void setAllExistsId(String allExistsId) {
        this.allExistsId = allExistsId;
    }

    public String getBatchGetId() {
        return batchGetId;
    }

    public void setBatchGetId(String batchGetId) {
        this.batchGetId = batchGetId;
    }
}
