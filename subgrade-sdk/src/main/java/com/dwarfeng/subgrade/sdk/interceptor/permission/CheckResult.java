package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.exception.PermissionDeniedException;

import java.util.List;
import java.util.Objects;

/**
 * 权限缺失信息。
 *
 * @author DwArFeng
 * @since 0.1.0-beta
 */
public class CheckResult {

    private HandlerException handlerException;
    private List<String> missingPermissions;

    public CheckResult() {
    }

    public CheckResult(HandlerException handlerException, List<String> missingPermissions) {
        this.handlerException = handlerException;
        this.missingPermissions = missingPermissions;
    }

    public void mayThrowContextException() throws Exception {
        if (Objects.nonNull(handlerException)) {
            throw handlerException;
        }
        if (Objects.nonNull(missingPermissions) && !missingPermissions.isEmpty()) {
            throw new PermissionDeniedException(missingPermissions);
        }
    }

    public HandlerException getHandlerException() {
        return handlerException;
    }

    public void setHandlerException(HandlerException handlerException) {
        this.handlerException = handlerException;
    }

    public List<String> getMissingPermissions() {
        return missingPermissions;
    }

    public void setMissingPermissions(List<String> missingPermissions) {
        this.missingPermissions = missingPermissions;
    }

    @Override
    public String toString() {
        return "PermissionMissingInfo{" +
                "handlerException=" + handlerException +
                ", missingPermissions=" + missingPermissions +
                '}';
    }
}
