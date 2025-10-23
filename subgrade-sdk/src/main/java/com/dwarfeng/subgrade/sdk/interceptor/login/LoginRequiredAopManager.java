package com.dwarfeng.subgrade.sdk.interceptor.login;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 登录增强管理器。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public interface LoginRequiredAopManager {

    /**
     * 从指定的 PJP 中获取登录 ID。
     *
     * @param pjp 指定的 PJP。
     * @return 从指定的 PJP 中获取的登录 ID。
     * @throws Throwable 方法执行过程中抛出的任何异常。
     */
    String getLoginId(ProceedingJoinPoint pjp) throws Throwable;

    /**
     * 当用户没有登录时执行的调度。
     *
     * @param pjp     指定的 PJP 上下文。
     * @param loginId 没有登录的用户 ID。
     * @return 返回的对象。
     * @throws Throwable 抛出的任何异常。
     */
    Object onNotLogin(ProceedingJoinPoint pjp, String loginId) throws Throwable;
}
