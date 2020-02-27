package com.dwarfeng.subgrade.sdk.interceptor.http;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.validation.BindingResult;

/**
 * 绑定检查增强管理器。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public interface BindingCheckAopManager {

    /**
     * 当指定的 BindingResult 有错误时进行的调度。
     *
     * @param pjp           相关的PJP。
     * @param bindingResult 指定的 BindingResult。
     * @return 返回的对象。
     * @throws Throwable 抛出的任何异常。
     */
    Object onHasError(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable;
}
