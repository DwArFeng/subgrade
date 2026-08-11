package com.dwarfeng.subgrade.basic.stack.exception;

import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessageKey;
import com.dwarfeng.subgrade.basic.internal.i18n.BasicMessages;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo;

import java.io.Serial;

/**
 * 分页异常。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class PagingException extends Exception {

    @Serial
    private static final long serialVersionUID = -1244826846110151662L;

    private final PagingInfo pagingInfo;

    public PagingException(PagingInfo pagingInfo) {
        super(BasicMessages.message(BasicMessageKey.PAGING_EXCEPTION_DETAIL, pagingInfo));
        this.pagingInfo = pagingInfo;
    }

    public PagingException(String message, PagingInfo pagingInfo) {
        super(message);
        this.pagingInfo = pagingInfo;
    }

    @Override
    public String getMessage() {
        return BasicMessages.message(BasicMessageKey.PAGING_EXCEPTION_DETAIL, pagingInfo);
    }
}
