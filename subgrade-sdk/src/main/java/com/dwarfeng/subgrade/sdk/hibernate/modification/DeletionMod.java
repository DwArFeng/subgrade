package com.dwarfeng.subgrade.sdk.hibernate.modification;

import com.dwarfeng.subgrade.stack.bean.Bean;

import java.util.List;

/**
 * 删除处理。
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface DeletionMod<E extends Bean> {

    /**
     * 提供当指定对象删除时，需要更新的所有对象。
     *
     * @param element 指定的对象。
     * @return 需要更新的对象组成的列表。
     */
    List<Object> updateBeforeDelete(E element);
}
