package com.dwarfeng.subgrade.data.stack.exception;

import java.io.Serial;

/**
 * 数据访问层异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class DaoException extends Exception {

    @Serial
    private static final long serialVersionUID = -728045042524851486L;

    public DaoException() {
        super();
    }

    protected DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

}
