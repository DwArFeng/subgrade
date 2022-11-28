package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 线上处理器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface OnlineHandler extends Handler {

    /**
     * 处理器是否在线。
     *
     * @return 处理器是否在线。
     * @throws HandlerException 处理器异常。
     */
    boolean isOnline() throws HandlerException;

    /**
     * 处理器上线。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void online() throws HandlerException;

    /**
     * 处理器下线。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void offline() throws HandlerException;
}
