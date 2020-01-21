package com.dwarfeng.subgrade.stack.exception;

/**
 * 主键获取异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class KeyFetchException extends Exception {

    private static final long serialVersionUID = -799575221601479850L;

    public KeyFetchException() {
    }

    public KeyFetchException(String message) {
        super(message);
    }

    public KeyFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyFetchException(Throwable cause) {
        super(cause);
    }

    protected KeyFetchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
