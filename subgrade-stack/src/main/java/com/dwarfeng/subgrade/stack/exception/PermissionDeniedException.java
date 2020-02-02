package com.dwarfeng.subgrade.stack.exception;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 权限拒绝异常。
 *
 * @author DwArFeng
 * @since 0.1.0-alpha
 */
public class PermissionDeniedException extends Exception {

    private static final long serialVersionUID = 6241543379412106596L;

    private List<String> missingPermissions;

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

    @Override
    public String getMessage() {
        String string = "null";
        if (Objects.nonNull(missingPermissions)) {
            string = Arrays.toString(missingPermissions.toArray());
        }
        return "Missing permission " + string;
    }
}
