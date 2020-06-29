package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.dao.BatchWriteDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * 使用 MyBatis 基于多对多关系实现的 RelationDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class MybatisBatchWriteDao<E extends Entity<?>> implements BatchWriteDao<E> {

    public static final String DEFAULT_WRITE_ID = "write";
    public static final String DEFAULT_BATCH_WRITE_ID = "batchWrite";

    private SqlSessionTemplate template;
    private String namespace;
    private String writeId;
    private String batchWriteId;

    public MybatisBatchWriteDao(
            @NonNull SqlSessionTemplate template, @NonNull String namespace, boolean defaultBatchOperation) {
        this(
                template, namespace, DEFAULT_WRITE_ID,
                defaultBatchOperation ? DEFAULT_WRITE_ID : DEFAULT_BATCH_WRITE_ID
        );
    }

    public MybatisBatchWriteDao(
            @NonNull SqlSessionTemplate template,
            @NonNull String namespace,
            @NonNull String writeId,
            @NonNull String batchWriteId) {
        this.template = template;
        this.namespace = namespace;
        this.writeId = writeId;
        this.batchWriteId = batchWriteId;
    }

    @Override
    public void write(E element) throws DaoException {
        try {
            internalWrite(element);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private void internalWrite(E element) {
        template.insert(concatId(writeId), element);
    }

    @Override
    public void batchWrite(List<E> elements) throws DaoException {
        try {
            if (Objects.equals(writeId, batchWriteId)) {
                for (E element : elements) {
                    internalWrite(element);
                }
            }
            template.insert(concatId(batchWriteId), elements);
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

    public String getBatchWriteId() {
        return batchWriteId;
    }

    public void setBatchWriteId(String batchWriteId) {
        this.batchWriteId = batchWriteId;
    }
}
