package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

import javax.servlet.http.HttpServletRequest;

/**
 * Token 处理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface TokenHandler extends Handler {

    /**
     * 在指定的 HTTP 请求中获取用户 ID。
     *
     * @param httpServletRequest 指定的 HTTP 请求。
     * @return 指定的 HTTP 请求中获取的用户 ID。
     * @throws HandlerException 处理器异常。
     */
    String getUserId(HttpServletRequest httpServletRequest) throws HandlerException;

    /**
     * 在指定的 HTTP 请求中获取登录 ID。
     *
     * @param httpServletRequest 指定的 HTTP 请求。
     * @return 指定的 HTTP 请求中获取的登录 ID。
     * @throws HandlerException 处理器异常。
     */
    String getLoginId(HttpServletRequest httpServletRequest) throws HandlerException;
}
