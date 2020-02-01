package com.dwarfeng.subgrade.sdk.hibernate.modification;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 删除处理。
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface Deletion<E> {

    /**
     * 提供当指定对象删除时，需要更新的所有对象。
     *
     * @param element 指定的对象。
     * @return 需要更新的对象组成的列表。
     */
    @NonNull
    List<Object> updateBeforeDelete(@NonNull E element);
}
