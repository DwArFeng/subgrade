package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

/**
 * 批量关系服务。
 *
 * <p>
 * 此种服务适合使用在一对多或者多对多的情形中，多对一的情况下，大多是直接更新实体，而不是用此种服务变更关系。
 *
 * @author DwArFeng
 * @since 0.2.4-beta
 */
public interface BatchRelationService<PK extends Key, CK extends Key> extends RelationService<PK, CK> {

    /**
     * 批量查询关系是否存在。
     *
     * <p>
     * 如果指定的父项与指定的子项全部存在关系，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部包含。
     * @throws ServiceException 服务异常。
     */
    boolean existsAllRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 批量查询关系是否存在。
     *
     * <p>
     * 如果指定的父项与指定的子项全部不存在关系，则返回 <code>true</code>；否则返回 <code>false</code>。
     *
     * @param pk  指定的父项。
     * @param cks 指定的子项组成的列表。
     * @return 是否全部不包含。
     * @throws ServiceException 服务异常。
     */
    boolean existsNonRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 批量添加关系。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void batchAddRelations(PK pk, List<CK> cks) throws ServiceException;

    /**
     * 批量删除关系。
     *
     * @param pk  父项主键。
     * @param cks 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void batchDeleteRelations(PK pk, List<CK> cks) throws ServiceException;
}
