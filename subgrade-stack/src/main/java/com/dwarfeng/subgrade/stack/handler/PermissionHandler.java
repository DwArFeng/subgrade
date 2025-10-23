package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

import java.util.List;

/**
 * 权限处理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface PermissionHandler extends Handler {

    /**
     * 查询用户是否拥有权限。
     *
     * @param userId     指定的用户 ID。
     * @param permission 指定的权限。
     * @return 指定的用户是否拥有指定的权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(String userId, String permission) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param userId      指定的用户 ID。
     * @param permissions 指定的权限组成的列表。
     * @return 指定的用户是否拥有指定的所有权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(String userId, List<String> permissions) throws HandlerException;

    /**
     * 查询用户缺失的权限。
     *
     * <p>
     * 如果用户不缺失权限，返回空列表。
     *
     * @param userId      指定的用户 ID。
     * @param permissions 指定的权限组成的列表。
     * @return 用户缺失的权限组成的列表。
     * @throws HandlerException 处理器异常。
     */
    List<String> getMissingPermissions(String userId, List<String> permissions) throws HandlerException;
}
