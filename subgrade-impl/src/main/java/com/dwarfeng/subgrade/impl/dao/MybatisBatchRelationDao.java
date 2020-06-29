package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.mybatis.BatchRelationInfo;
import com.dwarfeng.subgrade.sdk.mybatis.RelationInfo;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchRelationDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * 使用 MyBatis 基于多对多关系实现的 BatchRelationDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisBatchRelationDao<PK extends Key, CK extends Key> implements BatchRelationDao<PK, CK> {

    public static final String DEFAULT_EXISTS_RELATION_ID = "existsRelation";
    public static final String DEFAULT_ADD_RELATION_ID = "addRelation";
    public static final String DEFAULT_DELETE_RELATION_ID = "deleteRelation";
    public static final String DEFAULT_EXISTS_ALL_RELATION_ID = "existsAllRelation";
    public static final String DEFAULT_EXISTS_NON_RELATION_ID = "existsNonRelation";
    public static final String DEFAULT_BATCH_ADD_RELATION_ID = "batchAddRelation";
    public static final String DEFAULT_BATCH_DELETE_RELATION_ID = "batchDeleteRelation";

    private SqlSessionTemplate template;
    private String namespace;
    private String existsRelationId;
    private String addRelationId;
    private String deleteRelationId;
    private String existsAllRelationId;
    private String existsNonRelationId;
    private String batchAddRelationId;
    private String batchDeleteRelationId;

    public MybatisBatchRelationDao(SqlSessionTemplate template, String namespace, boolean defaultBatchOperation) {
        this(
                template, namespace, DEFAULT_EXISTS_RELATION_ID,
                DEFAULT_ADD_RELATION_ID, DEFAULT_DELETE_RELATION_ID,
                defaultBatchOperation ? DEFAULT_EXISTS_RELATION_ID : DEFAULT_EXISTS_ALL_RELATION_ID,
                defaultBatchOperation ? DEFAULT_EXISTS_RELATION_ID : DEFAULT_EXISTS_NON_RELATION_ID,
                defaultBatchOperation ? DEFAULT_ADD_RELATION_ID : DEFAULT_BATCH_ADD_RELATION_ID,
                defaultBatchOperation ? DEFAULT_DELETE_RELATION_ID : DEFAULT_BATCH_DELETE_RELATION_ID);
    }

    public MybatisBatchRelationDao(
            @NonNull SqlSessionTemplate template,
            @NonNull String namespace,
            @NonNull String existsRelationId,
            @NonNull String addRelationId,
            @NonNull String deleteRelationId,
            @NonNull String existsAllRelationId,
            @NonNull String existsNonRelationId,
            @NonNull String batchAddRelationId,
            @NonNull String batchDeleteRelationId) {
        this.template = template;
        this.namespace = namespace;
        this.existsRelationId = existsRelationId;
        this.addRelationId = addRelationId;
        this.deleteRelationId = deleteRelationId;
        this.existsAllRelationId = existsAllRelationId;
        this.existsNonRelationId = existsNonRelationId;
        this.batchAddRelationId = batchAddRelationId;
        this.batchDeleteRelationId = batchDeleteRelationId;
    }

    @Override
    public boolean existsRelation(PK pk, CK ck) throws DaoException {
        try {
            return internalExistsRelation(pk, ck);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private boolean internalExistsRelation(PK pk, CK ck) {
        return template.selectOne(concatId(existsRelationId), new RelationInfo<>(pk, ck));
    }

    @Override
    public void addRelation(PK pk, CK ck) throws DaoException {
        try {
            internalAddRelation(pk, ck);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalAddRelation(PK pk, CK ck) {
        template.update(concatId(addRelationId), new RelationInfo<>(pk, ck));
    }

    @Override
    public void deleteRelation(PK pk, CK ck) throws DaoException {
        try {
            internalDeleteRelation(pk, ck);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalDeleteRelation(PK pk, CK ck) {
        template.update(concatId(deleteRelationId), new RelationInfo<>(pk, ck));
    }

    @Override
    public boolean existsAllRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            if (Objects.equals(existsRelationId, existsAllRelationId)) {
                for (CK ck : cks) {
                    if (!internalExistsRelation(pk, ck)) {
                        return false;
                    }
                }
                return true;
            }
            return template.selectOne(concatId(existsAllRelationId), new BatchRelationInfo<>(pk, cks));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean existsNonRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            if (Objects.equals(existsRelationId, existsNonRelationId)) {
                for (CK ck : cks) {
                    if (internalExistsRelation(pk, ck)) {
                        return false;
                    }
                }
                return true;
            }
            return template.selectOne(concatId(existsNonRelationId), new BatchRelationInfo<>(pk, cks));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchAddRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            if (Objects.equals(addRelationId, batchAddRelationId)) {
                for (CK ck : cks) {
                    internalAddRelation(pk, ck);
                }
            }
            template.update(concatId(batchAddRelationId), new BatchRelationInfo<>(pk, cks));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDeleteRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            if (Objects.equals(deleteRelationId, batchDeleteRelationId)) {
                for (CK ck : cks) {
                    internalDeleteRelation(pk, ck);
                }
            }
            template.update(concatId(batchDeleteRelationId), new BatchRelationInfo<>(pk, cks));
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

    public String getExistsRelationId() {
        return existsRelationId;
    }

    public void setExistsRelationId(String existsRelationId) {
        this.existsRelationId = existsRelationId;
    }

    public String getAddRelationId() {
        return addRelationId;
    }

    public void setAddRelationId(String addRelationId) {
        this.addRelationId = addRelationId;
    }

    public String getDeleteRelationId() {
        return deleteRelationId;
    }

    public void setDeleteRelationId(String deleteRelationId) {
        this.deleteRelationId = deleteRelationId;
    }

    public String getExistsAllRelationId() {
        return existsAllRelationId;
    }

    public void setExistsAllRelationId(String existsAllRelationId) {
        this.existsAllRelationId = existsAllRelationId;
    }

    public String getExistsNonRelationId() {
        return existsNonRelationId;
    }

    public void setExistsNonRelationId(String existsNonRelationId) {
        this.existsNonRelationId = existsNonRelationId;
    }

    public String getBatchAddRelationId() {
        return batchAddRelationId;
    }

    public void setBatchAddRelationId(String batchAddRelationId) {
        this.batchAddRelationId = batchAddRelationId;
    }

    public String getBatchDeleteRelationId() {
        return batchDeleteRelationId;
    }

    public void setBatchDeleteRelationId(String batchDeleteRelationId) {
        this.batchDeleteRelationId = batchDeleteRelationId;
    }
}
