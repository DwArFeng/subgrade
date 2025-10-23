package com.dwarfeng.subgrade.sdk.interceptor.permission;

/**
 * Token 解析器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface TokenResolver {

    /**
     * 将指定的 token 解析为用户 ID。
     *
     * @param token 指定的 token。
     * @return 解析后的用户 ID。
     * @throws Exception 解析过程中发生的任何异常。
     */
    String resolve(String token) throws Exception;
}
