package com.dwarfeng.subgrade.stack.exception;

/**
 * 处理器异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class HandlerException extends Exception {

    private static final long serialVersionUID = -6670847431109214354L;

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
