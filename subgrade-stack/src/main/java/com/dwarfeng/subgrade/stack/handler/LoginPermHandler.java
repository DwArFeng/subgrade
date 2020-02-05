package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;

import java.util.List;

/**
 * 登录与权限处理器。
 *
 * @author DwArFeng
 * @since 0.1.2-beta
 */
public interface LoginPermHandler extends Handler {

    /**
     * 检查密码是否正确。
     *
     * @param userKey  指定的用户名。
     * @param password 指定的密码。
     * @return 密码是否正确。
     * @throws HandlerException 处理器异常。
     */
    boolean checkPassword(StringIdKey userKey, String password) throws HandlerException;

    /**
     * 登录。
     *
     * @param userKey  指定的用户名。
     * @param password 指定的密码。
     * @return 登录ID。
     * @throws HandlerException 处理器异常。
     */
    LongIdKey login(StringIdKey userKey, String password) throws HandlerException;

    /**
     * 登出。
     *
     * @param idKey 指定的登录状态主键。
     * @throws HandlerException 处理器异常。
     */
    void logout(LongIdKey idKey) throws HandlerException;

    /**
     * 判断指定的登录状态主键是否处于有效的登录状态。
     *
     * @param idKey 指定的登录状态主键。
     * @return 指定的登录状态主键是否处于有效的登录状态。
     * @throws HandlerException 处理器异常。
     */
    boolean isLogin(LongIdKey idKey) throws HandlerException;

    /**
     * 延长指定登录状态主键的超时日期。
     *
     * @param idKey 指定的登录状态主键。
     * @throws HandlerException 处理器异常。
     */
    void postpone(LongIdKey idKey) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param idKey          指定的登录状态主键。
     * @param permissionNode 指定的权限。
     * @return 指定的用户是否拥有指定的权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(LongIdKey idKey, String permissionNode) throws HandlerException;

    /**
     * 查询用户是否拥有权限。
     *
     * @param idKey           指定的登录状态主键。
     * @param permissionNodes 指定的权限组成的列表。
     * @return 指定的用户是否拥有指定的所有权限。
     * @throws HandlerException 处理器异常。
     */
    boolean hasPermission(LongIdKey idKey, List<String> permissionNodes) throws HandlerException;

    /**
     * 查询用户缺失的权限。
     * <p>如果用户不缺失权限，返回空列表。</p>
     *
     * @param idKey           指定的登录状态主键。
     * @param permissionNodes 指定的权限组成的列表。
     * @return 用户缺失的权限。
     * @throws HandlerException 处理器异常。
     */
    List<String> getMissingPermission(LongIdKey idKey, List<String> permissionNodes) throws HandlerException;
}
