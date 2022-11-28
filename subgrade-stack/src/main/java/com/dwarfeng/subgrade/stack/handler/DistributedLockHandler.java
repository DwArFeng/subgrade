package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 线上处理器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface DistributedLockHandler extends OnlineHandler, StartableHandler {

    /**
     * 驱动处理器是否正在持有锁。
     *
     * @return 驱动处理器是否正在持有锁。
     * @throws HandlerException 处理器异常。
     */
    boolean isLockHolding() throws HandlerException;

    /**
     * 处理器是否正在工作。
     *
     * @return 处理器是否正在工作。
     * @throws HandlerException 处理器异常。
     */
    boolean isWorking() throws HandlerException;
}
