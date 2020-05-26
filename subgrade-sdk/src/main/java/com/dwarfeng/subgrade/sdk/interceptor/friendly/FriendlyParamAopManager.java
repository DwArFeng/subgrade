package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 友好性参数增强管理器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface FriendlyParamAopManager {

    /**
     * 对入口参数进行友好性调整。
     *
     * <p>
     * 注意： 针对切入点入口参数 pjp，您只能调用其读取方法，而不能调用任何写入以及执行方法。
     *
     * @param pjp  切入点。
     * @param args 原始的入口参数。
     * @return 友好性调整后的入口参数。
     */
    Object[] processParam(ProceedingJoinPoint pjp, Object[] args);
}
