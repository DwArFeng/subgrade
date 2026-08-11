package com.dwarfeng.subgrade.data.stack.exception;

import com.dwarfeng.subgrade.data.internal.i18n.DataMessageKey;
import com.dwarfeng.subgrade.data.internal.i18n.DataMessages;

import java.io.Serial;

/**
 * 实体已经存在异常。
 *
 * @author DwArFeng
 * @since 1.1.8
 */
public class EntityExistedException extends DaoException {

    @Serial
    private static final long serialVersionUID = 14369803432063520L;

    private final Object key;

    public EntityExistedException(Object key) {
        super(DataMessages.message(DataMessageKey.ENTITY_EXISTED_DETAIL, key));
        this.key = key;
    }

    public EntityExistedException(Throwable cause, Object key) {
        super(DataMessages.message(DataMessageKey.ENTITY_EXISTED_DETAIL, key), cause);
        this.key = key;
    }

    @Override
    public String getMessage() {
        return DataMessages.message(DataMessageKey.ENTITY_EXISTED_DETAIL, key);
    }
}
