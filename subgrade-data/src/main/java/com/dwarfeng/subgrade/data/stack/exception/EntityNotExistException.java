package com.dwarfeng.subgrade.data.stack.exception;

import com.dwarfeng.subgrade.data.internal.i18n.DataMessageKey;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessages;

import java.io.Serial;

/**
 * 实体不存在异常。
 *
 * @author DwArFeng
 * @since 1.1.8
 */
public class EntityNotExistException extends DaoException {

    @Serial
    private static final long serialVersionUID = -3884501564327172700L;

    private final Object key;

    public EntityNotExistException(Object key) {
        super(DataMessages.message(DataMessageKey.ENTITY_NOT_EXIST_DETAIL, key));
        this.key = key;
    }

    public EntityNotExistException(Throwable cause, Object key) {
        super(DataMessages.message(DataMessageKey.ENTITY_NOT_EXIST_DETAIL, key), cause);
        this.key = key;
    }

    @Override
    public String getMessage() {
        return DataMessages.message(DataMessageKey.ENTITY_NOT_EXIST_DETAIL, key);
    }
}
