package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;

import java.util.List;

/**
 * 权限处理器。
 *
 * @author DwArFeng
 * @since 0.1.0-beta
 */
public interface PermissionHandler {

    /**
     * 查询用户是否拥有权限。
     *
     * @param userKey        指定的用户。
     * @param permissionNode 指定的权限。
     * @return 指定的用户是否拥有指定的权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(StringIdKey userKey, String permissionNode) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param userKey         指定的用户。
     * @param permissionNodes 指定的权限组成的列表。
     * @return 指定的用户是否拥有指定的所有权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(StringIdKey userKey, List<String> permissionNodes) throws HandlerException;

    /**
     * 查询用户缺失的权限。
     * <p>如果用户不缺失权限，返回空列表。</p>
     *
     * @param userKey         指定的用户。
     * @param permissionNodes 指定的权限组成的列表。
     * @return 用户缺失的权限。
     * @throws HandlerException 处理器异常。
     */
    List<String> getMissingPermission(StringIdKey userKey, List<String> permissionNodes) throws HandlerException;
}
