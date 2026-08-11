package com.dwarfeng.subgrade.web.stack.exception;

import com.dwarfeng.subgrade.web.internal.i18n.WebMessageKey;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessages;

import java.io.Serial;

/**
 * 权限拒绝异常。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class LoginFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = -5262296571604016957L;

    private final String loginId;

    public LoginFailedException(String loginId) {
        super(WebMessages.message(WebMessageKey.LOGIN_FAILED_DETAIL, loginId));
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
        super(WebMessages.message(WebMessageKey.LOGIN_FAILED_DETAIL, loginId), cause);
        this.loginId = loginId;
    }

    @Override
    public String getMessage() {
        return WebMessages.message(WebMessageKey.LOGIN_FAILED_DETAIL, loginId);
    }
}
