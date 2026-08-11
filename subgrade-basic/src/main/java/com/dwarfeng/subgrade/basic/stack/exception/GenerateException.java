package com.dwarfeng.subgrade.basic.stack.exception;

import java.io.Serial;

/**
 * 生成异常。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class GenerateException extends Exception {

    @Serial
    private static final long serialVersionUID = -6842360306136153388L;

    public GenerateException() {
    }

    public GenerateException(String message) {
        super(message);
    }

    public GenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateException(Throwable cause) {
        super(cause);
    }

    protected GenerateException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
