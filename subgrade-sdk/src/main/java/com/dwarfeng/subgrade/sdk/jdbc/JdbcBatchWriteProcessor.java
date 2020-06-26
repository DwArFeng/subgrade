package com.dwarfeng.subgrade.sdk.jdbc;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.1
 */
public interface JdbcBatchWriteProcessor<E extends Entity<?>> extends JdbcWriteProcessor<E> {

    boolean loopWrite();

    SQLAndParameter provideBatchWrite(List<E> elements) throws UnsupportedOperationException;
}
