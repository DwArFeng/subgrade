package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.BatchRelationDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用 Hibernate 基于多对多关系实现的 BatchRelationDao。
 * <p>该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。</p>
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public class HibernateBatchRelationDao<
        PK extends Key, PE extends Entity<PK>, CK extends Key, CE extends Entity<CK>,
        PPK extends Bean, PPE extends Bean, PCK extends Bean, PCE extends Bean
        > implements BatchRelationDao<PK, CK> {

    /**
     * 多对多关系在数据库中的连接类型。
     *
     * @author DwArFeng
     * @since 0.2.4-beta
     */
    public enum JoinType {
        /**
         * JoinTable注解在父类上。
         */
        JOIN_BY_PARENT,
        /**
         * JoinTable注解在子类上。
         */
        JOIN_BY_CHILD
    }

    private HibernateTemplate template;
    private BeanTransformer<PK, PPK> pkTransformer;
    private BeanTransformer<CK, PCK> ckTransformer;
    private BeanTransformer<PE, PPE> peTransformer;
    private BeanTransformer<CE, PCE> ceTransformer;
    private Class<PPE> classPPE;
    private Class<PCE> classPCE;
    private String parentProperty;
    private String childProperty;
    private JoinType joinType;

    public HibernateBatchRelationDao(
            @Nonnull HibernateTemplate template,
            @Nonnull BeanTransformer<PK, PPK> pkTransformer,
            @Nonnull BeanTransformer<CK, PCK> ckTransformer,
            @Nonnull BeanTransformer<PE, PPE> peTransformer,
            @Nonnull BeanTransformer<CE, PCE> ceTransformer,
            @Nonnull Class<PPE> classPPE,
            @Nonnull Class<PCE> classPCE,
            @Nonnull String parentProperty,
            @Nonnull String childProperty,
            @Nonnull JoinType joinType) {
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
            //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
            @SuppressWarnings("unchecked")
            Collection<PPE> ppes = (Collection<PPE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(pce, childProperty);
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
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PPE> ppes = (Collection<PPE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(pce, childProperty);
                ppes.add(ppe);
                assert pce != null;
                template.save(pce);
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PCE> pces = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
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
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PPE> ppes = (Collection<PPE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(pce, childProperty);
                ppes.remove(ppe);
                assert pce != null;
                template.save(pce);
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PCE> pces = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
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

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean existsAllRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            PPE ppe = template.get(classPPE, ppk);
            //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
            @SuppressWarnings("unchecked")
            Collection<PCE> pces = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
            List<CK> collect = pces.stream().map(ceTransformer::reverseTransform).map(CE::getKey).collect(Collectors.toList());
            return new HashSet<>(collect).containsAll(cks);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean existsNonRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            PPE ppe = template.get(classPPE, ppk);
            //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
            @SuppressWarnings("unchecked")
            Collection<PCE> pces = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
            List<CK> collect = pces.stream().map(ceTransformer::reverseTransform).map(CE::getKey).collect(Collectors.toList());
            for (CK ck : cks) {
                if (collect.contains(ck)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void batchAddRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            List<PCK> pcks = cks.stream().map(ckTransformer::transform).collect(Collectors.toList());
            PPE ppe = template.get(classPPE, ppk);
            List<PCE> pces = pcks.stream().map(pck -> template.get(classPCE, pck)).collect(Collectors.toList());
            if (joinType == JoinType.JOIN_BY_CHILD) {
                for (PCE pce : pces) {
                    //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                    @SuppressWarnings("unchecked")
                    Collection<PPE> ppes = (Collection<PPE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(pce, childProperty);
                    ppes.add(ppe);
                    template.save(pce);
                }
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PCE> property = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
                property.addAll(pces);
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
    public void batchDeleteRelations(PK pk, List<CK> cks) throws DaoException {
        try {
            PPK ppk = pkTransformer.transform(pk);
            List<PCK> pcks = cks.stream().map(ckTransformer::transform).collect(Collectors.toList());
            PPE ppe = template.get(classPPE, ppk);
            List<PCE> pces = pcks.stream().map(pck -> template.get(classPCE, pck)).collect(Collectors.toList());
            if (joinType == JoinType.JOIN_BY_CHILD) {
                for (PCE pce : pces) {
                    //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                    @SuppressWarnings("unchecked")
                    Collection<PPE> ppes = (Collection<PPE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(pce, childProperty);
                    ppes.remove(ppe);
                    template.save(pce);
                }
            } else if (joinType == JoinType.JOIN_BY_PARENT) {
                //如果配置的字段是正确的，则此处转换应该是对的，否则会抛出异常。
                @SuppressWarnings("unchecked")
                Collection<PCE> property = (Collection<PCE>) BeanUtilsBean.getInstance().getPropertyUtils().getProperty(ppe, parentProperty);
                property.removeAll(pces);
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

    public HibernateTemplate getTemplate() {
        return template;
    }

    public void setTemplate(@Nonnull HibernateTemplate template) {
        this.template = template;
    }

    public BeanTransformer<PK, PPK> getPkTransformer() {
        return pkTransformer;
    }

    public void setPkTransformer(@Nonnull BeanTransformer<PK, PPK> pkTransformer) {
        this.pkTransformer = pkTransformer;
    }

    public BeanTransformer<CK, PCK> getCkTransformer() {
        return ckTransformer;
    }

    public void setCkTransformer(@Nonnull BeanTransformer<CK, PCK> ckTransformer) {
        this.ckTransformer = ckTransformer;
    }

    public BeanTransformer<PE, PPE> getPeTransformer() {
        return peTransformer;
    }

    public void setPeTransformer(@Nonnull BeanTransformer<PE, PPE> peTransformer) {
        this.peTransformer = peTransformer;
    }

    public BeanTransformer<CE, PCE> getCeTransformer() {
        return ceTransformer;
    }

    public void setCeTransformer(@Nonnull BeanTransformer<CE, PCE> ceTransformer) {
        this.ceTransformer = ceTransformer;
    }

    public Class<PPE> getClassPPE() {
        return classPPE;
    }

    public void setClassPPE(@Nonnull Class<PPE> classPPE) {
        this.classPPE = classPPE;
    }

    public Class<PCE> getClassPCE() {
        return classPCE;
    }

    public void setClassPCE(@Nonnull Class<PCE> classPCE) {
        this.classPCE = classPCE;
    }

    public String getParentProperty() {
        return parentProperty;
    }

    public void setParentProperty(@Nonnull String parentProperty) {
        this.parentProperty = parentProperty;
    }

    public String getChildProperty() {
        return childProperty;
    }

    public void setChildProperty(@Nonnull String childProperty) {
        this.childProperty = childProperty;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(@Nonnull JoinType joinType) {
        this.joinType = joinType;
    }

    @Override
    public String toString() {
        return "HibernateMtmBatchRelationDao{" +
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
