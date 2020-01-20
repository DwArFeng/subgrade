package com.dwarfeng.subgrade.stack.exception;

/**
 * 数据访问层异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = -4921114286709749186L;

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
