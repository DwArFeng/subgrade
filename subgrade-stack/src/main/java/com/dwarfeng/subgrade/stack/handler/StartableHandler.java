package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 可启动处理器。
 *
 * <p>
 * 能够多次启停的处理器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface StartableHandler extends Handler {

    /**
     * 处理器是否启动。
     *
     * @return 处理器是否启动。
     * @throws HandlerException 处理器异常。
     */
    boolean isStarted() throws HandlerException;

    /**
     * 启动处理器。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void start() throws HandlerException;

    /**
     * 停止处理器。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void stop() throws HandlerException;
}
