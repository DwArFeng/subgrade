package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 登录增强管理器。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public interface LoginAopManager {

    /**
     * 从指定的PJP中获取登录ID。
     *
     * @param pjp 指定的PJP。
     * @return 从指定的PJP中获取的登录ID。
     * @throws Throwable 方法执行过程中抛出的任何异常。
     */
    LongIdKey getLoginId(ProceedingJoinPoint pjp) throws Throwable;

    /**
     * 当用户没有登录时执行的调度。
     *
     * @param pjp     指定的PJP上下文。
     * @param loginId 没有登录的用户ID。
     * @return 返回的对象。
     * @throws Throwable 抛出的任何异常。
     */
    Object onNotLogin(ProceedingJoinPoint pjp, LongIdKey loginId) throws Throwable;
}
