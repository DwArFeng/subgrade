package com.dwarfeng.subgrade.sdk.jdbc.processor;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.util.List;

/**
 * 批量写入处理器。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public interface BatchWriteProcessor<E extends Entity<?>> extends WriteProcessor<E> {

    boolean loopWrite();

    SQLAndParameter provideBatchWrite(List<E> elements) throws UnsupportedOperationException;
}
