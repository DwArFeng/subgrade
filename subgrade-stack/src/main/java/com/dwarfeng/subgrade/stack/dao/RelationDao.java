package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

/**
 * 关系数据访问层。
 * <p>此种数据访问层适合使用在一对多或者多对多的情形中，多对一的情况下，大多是直接update实体，而不是用此种数据访问层变更关系。</p>
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface RelationDao<PK extends Key, CK extends Key> extends Dao {

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
