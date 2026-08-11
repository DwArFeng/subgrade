package com.dwarfeng.subgrade.data.impl.dao.hibernate;

import com.dwarfeng.subgrade.basic.stack.bean.Bean;
import com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.data.internal.bean.BeanPropertyAccess;
import com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations;
import com.dwarfeng.subgrade.data.stack.dao.RelationDao;
import com.dwarfeng.subgrade.data.stack.exception.DaoException;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Objects;

/**
 * 使用 Hibernate 基于多对多关系实现的 RelationDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class HibernateRelationDao<
        PK extends Key, PE extends Entity<PK>, CK extends Key, CE extends Entity<CK>,
        PPK extends Bean, PPE extends Bean, PCK extends Bean, PCE extends Bean
        > implements RelationDao<PK, CK> {

    /**
     * 多对多关系在数据库中的连接类型。
     *
     * @author DwArFeng
     * @since 0.2.4-beta
     */
    public enum JoinType {
        /**
         * JoinTable 注解在父类上。
         */
        JOIN_BY_PARENT,
        /**
         * JoinTable 注解在子类上。
         */
        JOIN_BY_CHILD
    }

    @NotNull
    private HibernateOperations template;
    @NotNull
    private BeanTransformer<PK, PPK> pkTransformer;
    @NotNull
    private BeanTransformer<CK, PCK> ckTransformer;
    @NotNull
    private BeanTransformer<PE, PPE> peTransformer;
    @NotNull
    private BeanTransformer<CE, PCE> ceTransformer;
    @NotNull
    private Class<PPE> classPPE;
    @NotNull
    private Class<PCE> classPCE;
    @NotNull
    private String parentProperty;
    @NotNull
    private String childProperty;
    @NotNull
    private JoinType joinType;

    public HibernateRelationDao(
            @NotNull HibernateOperations template,
            @NotNull BeanTransformer<PK, PPK> pkTransformer,
            @NotNull BeanTransformer<CK, PCK> ckTransformer,
            @NotNull BeanTransformer<PE, PPE> peTransformer,
            @NotNull BeanTransformer<CE, PCE> ceTransformer,
            @NotNull Class<PPE> classPPE,
            @NotNull Class<PCE> classPCE,
            @NotNull String parentProperty,
            @NotNull String childProperty,
            @NotNull JoinType joinType
    ) {
        this.template = template;
        this.pkTransformer = pkTransformer;
        this.ckTransformer = ckTransformer;
        this.peTransformer = peTransformer;
        this.ceTransformer = ceTransformer;
        this.classPPE = classPPE;
        this.classPCE = classPCE;
        this.parentProperty = parentProperty;
        this.childProperty = childProperty;
        this.joinType = joinType;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean existsRelation(PK pk, CK ck) throws DaoException {
        try {
            PCK pck = ckTransformer.transform(ck);
            PCE pce = template.get(classPCE, pck);
            // 如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
            BeanPropertyAccess propertyUtils = BeanPropertyAccess.getInstance();
            @SuppressWarnings("unchecked")
            Collection<PPE> ppes = (Collection<PPE>) propertyUtils.getProperty(pce, childProperty);
            for (PPE ppe : ppes) {
                PE pe = peTransformer.reverseTransform(ppe);
                if (Objects.equals(pk, pe.getKey())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void addRelation(PK pk, CK ck) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            PCK pck = ckTransformer.transform(ck);
            PPE ppe = template.get(classPPE, ppk);
            PCE pce = template.get(classPCE, pck);
            if (joinType == JoinType.JOIN_BY_CHILD) {
                // 如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                BeanPropertyAccess propertyUtils = BeanPropertyAccess.getInstance();
                @SuppressWarnings("unchecked")
                Collection<PPE> ppes = (Collection<PPE>) propertyUtils.getProperty(pce, childProperty);
                ppes.add(ppe);
                assert pce != null;
                template.save(pce);
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                // 如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                BeanPropertyAccess propertyUtils = BeanPropertyAccess.getInstance();
                @SuppressWarnings("unchecked")
                Collection<PCE> pces = (Collection<PCE>) propertyUtils.getProperty(ppe, parentProperty);
                pces.add(pce);
                assert ppe != null;
                template.save(ppe);
            } else {
                throw new IllegalStateException("非法的 joinType: " + joinType);
            }
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void deleteRelation(PK pk, CK ck) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            PCK pck = ckTransformer.transform(ck);
            PPE ppe = template.get(classPPE, ppk);
            PCE pce = template.get(classPCE, pck);
            if (joinType == JoinType.JOIN_BY_CHILD) {
                // 如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                BeanPropertyAccess propertyUtils = BeanPropertyAccess.getInstance();
                @SuppressWarnings("unchecked")
                Collection<PPE> ppes = (Collection<PPE>) propertyUtils.getProperty(pce, childProperty);
                ppes.remove(ppe);
                assert pce != null;
                template.save(pce);
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                // 如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                BeanPropertyAccess propertyUtils = BeanPropertyAccess.getInstance();
                @SuppressWarnings("unchecked")
                Collection<PCE> pces = (Collection<PCE>) propertyUtils.getProperty(ppe, parentProperty);
                pces.remove(pce);
                assert ppe != null;
                template.save(ppe);
            } else {
                throw new IllegalStateException("非法的 joinType: " + joinType);
            }
            template.flush();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @NotNull
    public HibernateOperations getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull HibernateOperations template) {
        this.template = template;
    }

    @NotNull
    public BeanTransformer<PK, PPK> getPkTransformer() {
        return pkTransformer;
    }

    public void setPkTransformer(@NotNull BeanTransformer<PK, PPK> pkTransformer) {
        this.pkTransformer = pkTransformer;
    }

    @NotNull
    public BeanTransformer<CK, PCK> getCkTransformer() {
        return ckTransformer;
    }

    public void setCkTransformer(@NotNull BeanTransformer<CK, PCK> ckTransformer) {
        this.ckTransformer = ckTransformer;
    }

    @NotNull
    public BeanTransformer<PE, PPE> getPeTransformer() {
        return peTransformer;
    }

    public void setPeTransformer(@NotNull BeanTransformer<PE, PPE> peTransformer) {
        this.peTransformer = peTransformer;
    }

    @NotNull
    public BeanTransformer<CE, PCE> getCeTransformer() {
        return ceTransformer;
    }

    public void setCeTransformer(@NotNull BeanTransformer<CE, PCE> ceTransformer) {
        this.ceTransformer = ceTransformer;
    }

    @NotNull
    public Class<PPE> getClassPPE() {
        return classPPE;
    }

    public void setClassPPE(@NotNull Class<PPE> classPPE) {
        this.classPPE = classPPE;
    }

    @NotNull
    public Class<PCE> getClassPCE() {
        return classPCE;
    }

    public void setClassPCE(@NotNull Class<PCE> classPCE) {
        this.classPCE = classPCE;
    }

    @NotNull
    public String getParentProperty() {
        return parentProperty;
    }

    public void setParentProperty(@NotNull String parentProperty) {
        this.parentProperty = parentProperty;
    }

    @NotNull
    public String getChildProperty() {
        return childProperty;
    }

    public void setChildProperty(@NotNull String childProperty) {
        this.childProperty = childProperty;
    }

    @NotNull
    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(@NotNull JoinType joinType) {
        this.joinType = joinType;
    }

    @Override
    public String toString() {
        return "HibernateRelationDao{" +
                "template=" + template +
                ", pkTransformer=" + pkTransformer +
                ", ckTransformer=" + ckTransformer +
                ", peTransformer=" + peTransformer +
                ", ceTransformer=" + ceTransformer +
                ", classPPE=" + classPPE +
                ", classPCE=" + classPCE +
                ", parentProperty='" + parentProperty + '\'' +
                ", childProperty='" + childProperty + '\'' +
                ", joinType=" + joinType +
                '}';
    }
}
