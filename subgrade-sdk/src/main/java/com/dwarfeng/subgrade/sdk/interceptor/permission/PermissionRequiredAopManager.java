package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 权限增强管理器。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public interface PermissionRequiredAopManager {

    /**
     * 从指定的 PJP 中获取用户主键。
     *
     * @param pjp 指定的 PJP。
     * @return 从指定的 PJP 中获取的用户主键。
     * @throws Throwable 方法执行过程中抛出的任何异常。
     */
    StringIdKey getUserKey(ProceedingJoinPoint pjp) throws Throwable;

    /**
     * 当用户缺失权限时执行的调度。
     *
     * @param pjp                指定的 PJP 上下文。
     * @param userKey            缺失权限的用户主键。
     * @param missingPermissions 缺失的权限。
     * @return 返回的对象。
     * @throws Throwable 抛出的任何异常。
     */
    Object onMissingPermission(ProceedingJoinPoint pjp, StringIdKey userKey, List<String> missingPermissions)
            throws Throwable;
}
