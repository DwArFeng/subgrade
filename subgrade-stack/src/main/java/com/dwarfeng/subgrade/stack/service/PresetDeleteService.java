package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 预设删除服务。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface PresetDeleteService<E extends Entity<?>> extends PresetLookupService<E> {

    /**
     * 删除数据访问层中符合预设的所有对象。
     *
     * @param preset 指定的预设名称。
     * @param objs   预设相关的对象。
     * @throws ServiceException 服务异常。
     */
    void lookupDelete(String preset, Object[] objs) throws ServiceException;
}
