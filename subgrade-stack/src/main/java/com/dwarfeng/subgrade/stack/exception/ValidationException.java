package com.dwarfeng.subgrade.stack.exception;

/**
 * 验证异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 5722609722599731452L;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    protected ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
