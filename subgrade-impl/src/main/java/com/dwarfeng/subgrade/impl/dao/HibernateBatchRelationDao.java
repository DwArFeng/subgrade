package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.hibernate.modification.RelationMod;
import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchRelationDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.lang.NonNull;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 实现的 BatchBaseCache。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public class HibernateBatchRelationDao<
        PK extends Key, CK extends Key,
        PPK extends Bean, PCK extends Bean,
        PE extends Bean, CE extends Bean> implements BatchRelationDao<PK, CK> {

    private HibernateTemplate template;
    private BeanTransformer<PK, PPK> pkTransformer;
    private BeanTransformer<CK, PCK> ckTransformer;
    private Class<PE> classPE;
    private Class<CE> classCE;
    private RelationMod<PE, CE> relationMod;

    public HibernateBatchRelationDao(
            @NonNull HibernateTemplate template,
            @NonNull BeanTransformer<PK, PPK> pkTransformer,
            @NonNull BeanTransformer<CK, PCK> ckTransformer,
            @NonNull Class<PE> classPE,
            @NonNull Class<CE> classCE,
            @NonNull RelationMod<PE, CE> relationMod) {
        this.template = template;
        this.pkTransformer = pkTransformer;
        this.ckTransformer = ckTransformer;
        this.classPE = classPE;
        this.classCE = classCE;
        this.relationMod = relationMod;
    }

    @Override
    public void addRelation(PK pk, CK ck) throws DaoException {
        try {
            PE pe = template.get(classPE, pkTransformer.transform(pk));
            CE ce = template.get(classCE, ckTransformer.transform(ck));
            assert pe != null;
            assert ce != null;
            List<Object> objects = relationMod.updateOnAdd(pe, ce);
            objects.forEach(template::update);
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteRelation(PK pk, CK ck) throws DaoException {
        try {
            PE pe = template.get(classPE, pkTransformer.transform(pk));
            CE ce = template.get(classCE, ckTransformer.transform(ck));
            assert pe != null;
            assert ce != null;
            List<Object> objects = relationMod.updateOnDelete(pe, ce);
            objects.forEach(template::update);
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchAddRelation(PK pk, List<CK> cks) throws DaoException {
        try {
            PE pe = template.get(classPE, pkTransformer.transform(pk));
            List<CE> ces = cks.stream().map(ck -> template.get(classCE, ckTransformer.transform(ck))).collect(Collectors.toList());
            assert pe != null;
            Optional<List<Object>> reduce = ces.stream().map(ce -> relationMod.updateOnAdd(pe, ce)).reduce((a, b) -> {
                a.addAll(b);
                return a;
            });
            reduce.ifPresent(objects -> objects.forEach(template::update));
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void batchDeleteRelation(PK pk, List<CK> cks) throws DaoException {
        try {
            PE pe = template.get(classPE, pkTransformer.transform(pk));
            List<CE> ces = cks.stream().map(ck -> template.get(classCE, ckTransformer.transform(ck))).collect(Collectors.toList());
            assert pe != null;
            Optional<List<Object>> reduce = ces.stream().map(ce -> relationMod.updateOnDelete(pe, ce)).reduce((a, b) -> {
                a.addAll(b);
                return a;
            });
            reduce.ifPresent(objects -> objects.forEach(template::update));
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@NonNull HibernateTemplate template) {
        this.template = template;
    }

    public BeanTransformer<PK, PPK> getPkTransformer() {
        return pkTransformer;
    }

    public void setPkTransformer(@NonNull BeanTransformer<PK, PPK> pkTransformer) {
        this.pkTransformer = pkTransformer;
    }

    public BeanTransformer<CK, PCK> getCkTransformer() {
        return ckTransformer;
    }

    public void setCkTransformer(@NonNull BeanTransformer<CK, PCK> ckTransformer) {
        this.ckTransformer = ckTransformer;
    }

    public Class<PE> getClassPE() {
        return classPE;
    }

    public void setClassPE(@NonNull Class<PE> classPE) {
        this.classPE = classPE;
    }

    public Class<CE> getClassCE() {
        return classCE;
    }

    public void setClassCE(@NonNull Class<CE> classCE) {
        this.classCE = classCE;
    }

    public RelationMod<PE, CE> getRelationMod() {
        return relationMod;
    }

    public void setRelationMod(@NonNull RelationMod<PE, CE> relationMod) {
        this.relationMod = relationMod;
    }
}
