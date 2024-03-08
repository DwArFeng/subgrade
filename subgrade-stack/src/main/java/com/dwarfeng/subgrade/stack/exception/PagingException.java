package com.dwarfeng.subgrade.stack.exception;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

/**
 * 分页异常。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public class PagingException extends Exception {

    private static final long serialVersionUID = -8313160693591098021L;

    private final PagingInfo pagingInfo;

    public PagingException(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    public PagingException(String message, PagingInfo pagingInfo) {
        super(message);
        this.pagingInfo = pagingInfo;
    }

    @Override
    public String getMessage() {
        return "分页异常, 相关分页信息: " + pagingInfo;
    }
}
