package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 关系数据访问层。
 * <p>此种数据访问层适合使用在多对多的情形中。</p>
 * <p>一对多的情形，使用 BaseDao 就可以维护两个实体之间的关系。</p>
 * <p>对于查询方面，关系数据访问层仅提供二者关系是否存在的查询，其余查询可以通过PresetLookupDao进行实现。</p>
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface RelationDao<PK extends Key, CK extends Key> extends Dao {

    /**
     * 判断指定的父项和子项有没有关系。
     *
     * @param pk 指定的父项对应的键。
     * @param ck 指定的子项对应的键。
     * @return 父项与子项是否存在关系。
     * @throws DaoException 数据访问异常。
     */
    boolean existsRelation(PK pk, CK ck) throws DaoException;

    /**
     * 添加父项与子项的关联。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws DaoException 数据访问异常。
     */
    void addRelation(PK pk, CK ck) throws DaoException;

    /**
     * 删除父项与子项的关联。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws DaoException 数据访问异常。
     */
    void deleteRelation(PK pk, CK ck) throws DaoException;
}
