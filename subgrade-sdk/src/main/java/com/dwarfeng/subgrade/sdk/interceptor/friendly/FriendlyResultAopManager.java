package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 友好性结果增强管理器。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface FriendlyResultAopManager {

    /**
     * 对返回对象进行友好性调整。
     *
     * <p>
     * 注意： 针对切入点入口参数 pjp，您只能调用其读取方法，而不能调用任何写入以及执行方法。
     *
     * @param pjp    切入点。
     * @param result 原始的返回对象。
     * @return 友好性调整后的返回对象。
     */
    Object processResult(ProceedingJoinPoint pjp, Object result);
}
