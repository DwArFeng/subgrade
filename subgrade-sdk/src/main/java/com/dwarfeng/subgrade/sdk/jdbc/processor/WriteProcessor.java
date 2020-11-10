package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

/**
 * 写入处理器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface WriteProcessor<E extends Entity<?>> {

    SQLAndParameter provideWrite(E element);
}
