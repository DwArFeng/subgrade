package com.dwarfeng.subgrade.data.sdk.jdbc.processor;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;

/**
 * 写入处理器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface WriteProcessor<E extends Entity<?>> {

    SQLAndParameter provideWrite(E element);
}
