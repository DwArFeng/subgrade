package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

/**
 * 权限拒绝异常。
 *
 * @author DwArFeng
 * @since 0.1.0-alpha
 */
public class LoginFailedException extends Exception {

    private static final long serialVersionUID = 6241543379412106596L;

    private final LongIdKey loginId;

    public LoginFailedException(LongIdKey loginId) {
        this.loginId = loginId;
    }

    public LoginFailedException(String message, LongIdKey loginId) {
        super(message);
        this.loginId = loginId;
    }

    public LoginFailedException(String message, Throwable cause, LongIdKey loginId) {
        super(message, cause);
        this.loginId = loginId;
    }

    public LoginFailedException(Throwable cause, LongIdKey loginId) {
        super(cause);
        this.loginId = loginId;
    }

    @Override
    public String getMessage() {
        return "Not login " + loginId;
    }
}
