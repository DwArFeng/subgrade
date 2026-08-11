package com.dwarfeng.subgrade.basic.stack.exception;

import java.io.Serial;

/**
 * 处理器异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class HandlerException extends Exception {

    @Serial
    private static final long serialVersionUID = 9024336044047766548L;

    public HandlerException() {
        super();
    }

    protected HandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }
}
