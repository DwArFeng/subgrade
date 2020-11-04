package com.dwarfeng.subgrade.stack.exception;

/**
 * 实体不存在异常。
 *
 * @author DwArFeng
 * @since 1.1.8
 */
public class EntityNotExistException extends DaoException {

    private static final long serialVersionUID = 4228715321426041160L;

    private final Object key;

    public EntityNotExistException(Object key) {
        this.key = key;
    }

    public EntityNotExistException(Throwable cause, Object key) {
        super(cause);
        this.key = key;
    }

    @Override
    public String getMessage() {
        return "主键 " + key + "对应的实体不存在";
    }
}
