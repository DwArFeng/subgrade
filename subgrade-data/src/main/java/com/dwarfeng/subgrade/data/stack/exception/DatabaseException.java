package com.dwarfeng.subgrade.data.stack.exception;

import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;

import java.io.Serial;

/**
 * 数据库异常。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
public class DatabaseException extends HandlerException {

    @Serial
    private static final long serialVersionUID = 3830474854341642006L;

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
