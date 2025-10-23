package com.dwarfeng.subgrade.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

import java.util.Map;

/**
 * 登录处理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface LoginHandler extends Handler {

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
     * <p>
     * 需要注意的是，返回的结果 <b>必须具有一定的随机性，不得具有较为显著的规律</b>。<br>
     * 如果使用自增主键、雪花 ID 等具有显著规律的主键生成器，将会导致安全问题。
     *
     * @param userId   指定的用户 ID。
     * @param password 指定的密码。
     * @return 登录后生成的登录 ID。
     * @throws HandlerException 处理器异常。
     */
    String login(String userId, String password) throws HandlerException;

    /**
     * 登录。
     *
     * <p>
     * 需要注意的是，返回的结果 <b>必须具有一定的随机性，不得具有较为显著的规律</b>。<br>
     * 如果使用自增主键、雪花 ID 等具有显著规律的主键生成器，将会导致安全问题。
     *
     * @param userId        指定的用户 ID。
     * @param password      指定的密码。
     * @param extraParamMap 额外参数。
     * @return 登录后生成的登录 ID。
     * @throws HandlerException 处理器异常。
     */
    default String login(String userId, String password, Map<String, String> extraParamMap) throws HandlerException {
        return login(userId, password);
    }

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
}
