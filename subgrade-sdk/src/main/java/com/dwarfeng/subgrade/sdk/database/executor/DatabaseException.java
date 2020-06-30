package com.dwarfeng.subgrade.sdk.database.executor;

/**
 * 数据库异常。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = -438119495183581948L;

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
