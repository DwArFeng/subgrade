package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import java.util.List;

/**
 * 批量关系数据访问层。
 * <p>此种数据访问层适合使用在多对多的情形中。</p>
 * <p>一对多的情形，使用 BaseDao 就可以维护两个实体之间的关系。</p>
 * <p>对于查询方面，关系数据访问层仅提供二者关系是否存在的查询，其余查询可以通过PresetLookupDao进行实现。</p>
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface BatchRelationDao<PK extends Key, CK extends Key> extends RelationDao<PK, CK> {

    /**
     * 判断指定的父项是否包含指定的全部子项。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部包含。
     * @throws DaoException 数据访问异常。
     */
    boolean existsAllRelations(PK pk, List<CK> cks) throws DaoException;

    /**
     * 判断指定的父项是否不包含指定的全部子项。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部不包含。
     * @throws DaoException 数据访问异常。
     */
    boolean existsNonRelations(PK pk, List<CK> cks) throws DaoException;

    /**
     * 添加父项与子项的关联。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws DaoException 数据访问异常。
     */
    void batchAddRelations(PK pk, List<CK> cks) throws DaoException;

    /**
     * 删除父项与子项的关联。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws DaoException 数据访问异常。
     */
    void batchDeleteRelations(PK pk, List<CK> cks) throws DaoException;
}
