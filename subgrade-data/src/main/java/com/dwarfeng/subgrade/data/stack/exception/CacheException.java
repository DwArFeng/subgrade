package com.dwarfeng.subgrade.data.stack.exception;

import java.io.Serial;

/**
 * 缓存异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class CacheException extends Exception {

    @Serial
    private static final long serialVersionUID = 6733975947263404450L;

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
