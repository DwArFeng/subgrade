package com.dwarfeng.subgrade.sdk.jdbc;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface JdbcWriteProcessor<E extends Entity<?>> {

    SQLAndParameter provideWrite(E element);
}
