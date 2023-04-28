package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;

import javax.servlet.http.HttpServletRequest;

/**
 * Token 处理器。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public interface TokenHandler extends Handler {

    /**
     * 获取指定请求的 Token ID。
     *
     * @param httpServletRequest 指定的请求。
     * @return 指定请求的 Token ID。
     * @throws HandlerException 处理器异常。
     */
    Long getTokenId(HttpServletRequest httpServletRequest) throws HandlerException;

    /**
     * 获取指定请求的登录主键。
     *
     * @param httpServletRequest 指定的请求。
     * @return 指定请求的登录主键。
     * @throws HandlerException 处理器异常。
     */
    LongIdKey getLoginKey(HttpServletRequest httpServletRequest) throws HandlerException;

    /**
     * 获取指定请求的用户主键。
     *
     * @param httpServletRequest 指定的请求。
     * @return 指定请求的用户主键。
     * @throws HandlerException 处理器异常。
     */
    StringIdKey getUserKey(HttpServletRequest httpServletRequest) throws HandlerException;
}
