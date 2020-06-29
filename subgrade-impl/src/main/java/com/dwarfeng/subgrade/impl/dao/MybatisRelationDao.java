package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.mybatis.RelationInfo;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.RelationDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.NonNull;

/**
 * 使用 MyBatis 基于多对多关系实现的 RelationDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisRelationDao<PK extends Key, CK extends Key> implements RelationDao<PK, CK> {

    public static final String DEFAULT_EXISTS_RELATION_ID = "existsRelation";
    public static final String DEFAULT_ADD_RELATION_ID = "addRelation";
    public static final String DEFAULT_DELETE_RELATION_ID = "deleteRelation";

    private SqlSessionTemplate template;
    private String namespace;
    private String existsRelationId;
    private String addRelationId;
    private String deleteRelationId;

    public MybatisRelationDao(@NonNull SqlSessionTemplate template, @NonNull String namespace) {
        this(template, namespace, DEFAULT_EXISTS_RELATION_ID, DEFAULT_ADD_RELATION_ID, DEFAULT_DELETE_RELATION_ID);
    }

    public MybatisRelationDao(
            @NonNull SqlSessionTemplate template,
            @NonNull String namespace,
            @NonNull String existsRelationId,
            @NonNull String addRelationId,
            @NonNull String deleteRelationId) {
        this.template = template;
        this.namespace = namespace;
        this.existsRelationId = existsRelationId;
        this.addRelationId = addRelationId;
        this.deleteRelationId = deleteRelationId;
    }

    @Override
    public boolean existsRelation(PK pk, CK ck) throws DaoException {
        try {
            return template.selectOne(concatId(existsRelationId), new RelationInfo<>(pk, ck));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void addRelation(PK pk, CK ck) throws DaoException {
        try {
            template.update(concatId(addRelationId), new RelationInfo<>(pk, ck));
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteRelation(PK pk, CK ck) throws DaoException {
        try {
            template.update(concatId(deleteRelationId), new RelationInfo<>(pk, ck));
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
}
