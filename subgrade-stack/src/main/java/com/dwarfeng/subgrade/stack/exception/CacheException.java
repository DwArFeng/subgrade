package com.dwarfeng.subgrade.stack.exception;

/**
 * 缓存异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class CacheException extends Exception {

    private static final long serialVersionUID = -799575221601479850L;

    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    protected CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
