package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;

/**
 * Token 解析器。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public interface TokenResolver {

    /**
     * 将指定的 token 解析为用户主键。
     *
     * @param token 指定的 token。
     * @return 解析后的用户主键。
     * @throws Exception 解析过程中发生的任何异常。
     */
    StringIdKey resolve(String token) throws Exception;
}
