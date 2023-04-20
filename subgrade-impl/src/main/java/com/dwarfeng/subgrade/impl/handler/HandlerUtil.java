package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 线上处理器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public final class HandlerUtil {

    /**
     * 转换传入的异常并抛出。
     *
     * <p>
     * 如果传入的异常是 {@link HandlerException}，则直接抛出。<br>
     * 否则，抛出 <code>new HandlerException(e)</code>
     *
     * @param e 传入的异常。
     * @throws HandlerException 抛出的处理器异常。
     */
    public static void transformAndThrowException(Exception e) throws HandlerException {
        if (e instanceof HandlerException) {
            throw (HandlerException) e;
        }
        throw new HandlerException(e);
    }

    private HandlerUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
