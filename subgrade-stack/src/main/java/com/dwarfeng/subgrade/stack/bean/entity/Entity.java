package com.dwarfeng.subgrade.stack.bean.entity;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * Entity 接口。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Entity<K extends Key> extends Bean {

    /**
     * 获取主键。
     *
     * @return 实体的主键。
     */
    K getKey();

    /**
     * 设置实体的主键。
     *
     * @param key 被设置的主键。
     */
    void setKey(K key);

}
