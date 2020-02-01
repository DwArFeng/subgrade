package com.dwarfeng.subgrade.sdk.hibernate.modification;

import com.dwarfeng.subgrade.stack.bean.Bean;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 删除处理。
 *
 * @author DwArFeng
 * @since 0.0.7-beta
 */
public interface RelationMod<PE extends Bean, CE extends Bean> {

    /**
     * 提供当对象建立关系时需要更新的对象。
     *
     * @param pe 父对象。
     * @param ce 子对象。
     * @return 需要更新的对象。
     */
    @NonNull
    List<Object> updateOnAdd(@NonNull PE pe, @NonNull CE ce);

    /**
     * 提供当对象删除关系时需要更新的对象。
     *
     * @param pe 父对象。
     * @param ce 子对象。
     * @return 需要更新的对象。
     */
    @NonNull
    List<Object> updateOnDelete(@NonNull PE pe, @NonNull CE ce);
}
