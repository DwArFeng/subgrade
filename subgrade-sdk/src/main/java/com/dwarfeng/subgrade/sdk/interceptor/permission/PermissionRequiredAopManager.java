package com.dwarfeng.subgrade.sdk.interceptor.permission;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 权限增强管理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface PermissionRequiredAopManager {

    /**
     * 从指定的 PJP 中获取用户 ID。
     *
     * @param pjp 指定的 PJP。
     * @return 从指定的 PJP 中获取的用户 ID。
     * @throws Throwable 方法执行过程中抛出的任何异常。
     */
    String getUserId(ProceedingJoinPoint pjp) throws Throwable;

    /**
     * 当用户缺失权限时执行的调度。
     *
     * @param pjp                指定的 PJP 上下文。
     * @param userId             缺失权限的用户 ID。
     * @param missingPermissions 缺失的权限组成的列表。
     * @return 返回的对象。
     * @throws Throwable 抛出的任何异常。
     */
    Object onMissingPermission(ProceedingJoinPoint pjp, String userId, List<String> missingPermissions)
            throws Throwable;
}
