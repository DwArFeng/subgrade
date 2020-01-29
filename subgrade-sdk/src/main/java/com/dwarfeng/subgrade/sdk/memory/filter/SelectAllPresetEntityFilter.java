package com.dwarfeng.subgrade.sdk.memory.filter;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * 全选预设实体过滤器。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public class SelectAllPresetEntityFilter<E extends Entity<?>> implements PresetEntityFilter<E> {

    @Override
    public boolean accept(E entity, String preset, Object[] objs) {
        return true;
    }
}
