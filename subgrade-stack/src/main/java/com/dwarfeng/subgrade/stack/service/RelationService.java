package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 关系服务。
 * <p>此种服务适合使用在一对多或者多对多的情形中，多对一的情况下，大多是直接update实体，而不是用此种服务变更关系。</p>
 *
 * @author DwArFeng
 * @since 0.2.3-beta
 */
public interface RelationService<PK extends Key, CK extends Key> extends Service {

    /**
     * 判断指定的父项和子项有没有关系。
     *
     * @param pk 指定的父项对应的键。
     * @param ck 指定的子项对应的键。
     * @return 父项与子项是否存在关系。
     * @throws ServiceException 服务异常。
     */
    boolean existsRelation(PK pk, CK ck) throws ServiceException;

    /**
     * 添加父项与子项的关联。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void addRelation(PK pk, CK ck) throws ServiceException;

    /**
     * 删除父项与子项的关联。
     *
     * @param pk 父项主键。
     * @param ck 子项主键列表。
     * @throws ServiceException 服务异常。
     */
    void deleteRelation(PK pk, CK ck) throws ServiceException;
}
