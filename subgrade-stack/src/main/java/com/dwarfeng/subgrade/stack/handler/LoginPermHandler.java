package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

import java.util.List;

/**
 * 登录与权限处理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface LoginPermHandler extends Handler {

    /**
     * 检查密码是否正确。
     *
     * @param userId   指定的用户 ID。
     * @param password 指定的密码。
     * @return 密码是否正确。
     * @throws HandlerException 处理器异常。
     */
    boolean checkPassword(String userId, String password) throws HandlerException;

    /**
     * 登录。
     *
     * @param userId   指定的用户 ID。
     * @param password 指定的密码。
     * @return 登录 ID。
     * @throws HandlerException 处理器异常。
     */
    String login(String userId, String password) throws HandlerException;

    /**
     * 登出。
     *
     * @param loginId 指定的登录 ID。
     * @throws HandlerException 处理器异常。
     */
    void logout(String loginId) throws HandlerException;

    /**
     * 判断指定的登录 ID 是否处于有效的登录状态。
     *
     * @param loginId 指定的登录 ID。
     * @return 指定的登录 ID 是否处于有效的登录状态。
     * @throws HandlerException 处理器异常。
     */
    boolean isLogin(String loginId) throws HandlerException;

    /**
     * 延长指定登录 ID 的超时日期。
     *
     * @param loginId 指定的登录 ID。
     * @throws HandlerException 处理器异常。
     */
    void postpone(String loginId) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param loginId    指定的登录 ID。
     * @param permission 指定的权限。
     * @return 指定的用户是否拥有指定的权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(String loginId, String permission) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param loginId     指定的登录 ID。
     * @param permissions 指定的权限组成的列表。
     * @return 指定的用户是否拥有指定的所有权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(String loginId, List<String> permissions) throws HandlerException;

    /**
     * 查询用户缺失的权限。
     * <p>
     * 如果用户不缺失权限，返回空列表。
     *
     * @param loginId     指定的登录 ID。
     * @param permissions 指定的权限组成的列表。
     * @return 用户缺失的权限 ID 组成的列表。
     * @throws HandlerException 处理器异常。
     */
    List<String> getMissingPermission(String loginId, List<String> permissions) throws HandlerException;
}
