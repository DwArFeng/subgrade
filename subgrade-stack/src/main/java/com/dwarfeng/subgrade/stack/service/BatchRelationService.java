package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

/**
 * 批量关系服务。
 * <p>此种服务适合使用在一对多或者多对多的情形中，多对一的情况下，大多是直接update实体，而不是用此种服务变更关系。</p>
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public interface BatchRelationService<PK extends Key, CK extends Key> extends RelationService<PK, CK> {

    /**
     * 判断指定的父项是否包含指定的全部子项。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部包含。
     * @throws ServiceException 服务异常。
     */
    boolean existsAllRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 判断指定的父项是否不包含指定的全部子项。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部不包含。
     * @throws ServiceException 服务异常。
     */
    boolean existsNonRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 添加父项与子项的关联。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void batchAddRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 删除父项与子项的关联。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void batchDeleteRelations(PK pk, List<CK> cks) throws ServiceException;
}
