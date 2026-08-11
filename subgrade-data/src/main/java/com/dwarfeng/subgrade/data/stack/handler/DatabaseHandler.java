package com.dwarfeng.subgrade.data.stack.handler;

import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.basic.stack.handler.Handler;

import org.jetbrains.annotations.NotNull;

/**
 * 数据库处理器。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public interface DatabaseHandler<T> extends Handler {

    /**
     * 执行数据库任务。
     *
     * @param databaseTask 指定的数据库任务。
     * @return 数据库任务执行返回的值。
     * @throws HandlerException 处理器异常。
     */
    T executeTask(@NotNull DatabaseTask<? extends T> databaseTask) throws HandlerException;
}
