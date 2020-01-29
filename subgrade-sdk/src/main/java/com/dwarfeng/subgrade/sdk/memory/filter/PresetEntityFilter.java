package com.dwarfeng.subgrade.sdk.memory.filter;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * 预设实体过滤器。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface PresetEntityFilter<E extends Entity<?>> {

    /**
     * 判断实体是否能通过指定的预设。
     *
     * @param entity 指定的实体。
     * @param preset 指定的映射。
     * @param objs   与映射相关的对象数组。
     * @return 实体是否能通过制定的预设。
     */
    boolean accept(E entity, String preset, Object[] objs);
}
