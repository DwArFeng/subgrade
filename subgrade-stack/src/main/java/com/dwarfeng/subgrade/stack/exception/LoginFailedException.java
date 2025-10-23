package com.dwarfeng.subgrade.stack.exception;

/**
 * 权限拒绝异常。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class LoginFailedException extends Exception {

    private final String loginId;

    public LoginFailedException(String loginId) {
        this.loginId = loginId;
    }

    public LoginFailedException(String message, String loginId) {
        super(message);
        this.loginId = loginId;
    }

    public LoginFailedException(String message, Throwable cause, String loginId) {
        super(message, cause);
        this.loginId = loginId;
    }

    public LoginFailedException(Throwable cause, String loginId) {
        super(cause);
        this.loginId = loginId;
    }

    @Override
    public String getMessage() {
        return "Not login " + loginId;
    }
}
