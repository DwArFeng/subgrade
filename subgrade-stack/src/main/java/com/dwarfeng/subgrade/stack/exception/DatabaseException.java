package com.dwarfeng.subgrade.stack.exception;

/**
 * 数据库异常。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class DatabaseException extends HandlerException {

    private static final long serialVersionUID = -3223441677014919224L;

    public DatabaseException() {
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
