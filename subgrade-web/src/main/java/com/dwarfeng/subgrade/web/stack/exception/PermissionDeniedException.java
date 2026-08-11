package com.dwarfeng.subgrade.web.stack.exception;

import com.dwarfeng.subgrade.web.internal.i18n.WebMessageKey;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessages;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * 权限拒绝异常。
 *
 * @author DwArFeng
 * @since 0.1.0-alpha
 */
public class PermissionDeniedException extends Exception {

    @Serial
    private static final long serialVersionUID = -8506697820150798238L;

    private final List<String> missingPermissions;

    public PermissionDeniedException(List<String> missingPermissions) {
        this.missingPermissions = missingPermissions;
    }

    public PermissionDeniedException(String message, List<String> missingPermissions) {
        super(message);
        this.missingPermissions = missingPermissions;
    }

    public PermissionDeniedException(String message, Throwable cause, List<String> missingPermissions) {
        super(message, cause);
        this.missingPermissions = missingPermissions;
    }

    public PermissionDeniedException(Throwable cause, List<String> missingPermissions) {
        super(cause);
        this.missingPermissions = missingPermissions;
    }

    private static String formatPermissions(List<String> missingPermissions) {
        return missingPermissions == null ? "null" : Arrays.toString(missingPermissions.toArray());
    }

    @Override
    public String getMessage() {
        return WebMessages.message(WebMessageKey.PERMISSION_DENIED_DETAIL, formatPermissions(missingPermissions));
    }
}
