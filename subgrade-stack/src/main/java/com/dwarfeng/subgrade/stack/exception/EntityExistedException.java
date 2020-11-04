package com.dwarfeng.subgrade.stack.exception;

/**
 * 实体已经存在异常。
 *
 * @author DwArFeng
 * @since 1.1.8
 */
public class EntityExistedException extends DaoException {

    private static final long serialVersionUID = 397108282392355282L;

    private final Object key;

    public EntityExistedException(Object key) {
        this.key = key;
    }

    public EntityExistedException(Throwable cause, Object key) {
        super(cause);
        this.key = key;
    }

    @Override
    public String getMessage() {
        return "主键 " + key + "对应的实体已经存在";
    }
}
