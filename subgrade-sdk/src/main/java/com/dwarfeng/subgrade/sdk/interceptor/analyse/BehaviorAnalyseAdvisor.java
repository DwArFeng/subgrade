package com.dwarfeng.subgrade.sdk.interceptor.analyse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 方法行为分析增强。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BehaviorAnalyseAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorAnalyseAdvisor.class);

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse) || @within(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.debug("进入增强方法");
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        Object result = null;
        LOGGER.debug("获得方法名称 " + methodName);
        LOGGER.debug("获得方法参数");
        if (pjp.getArgs().length == 0) {
            LOGGER.debug("\t没有参数");
        } else {
            for (int i = 0; i < pjp.getArgs().length; i++) {
                LOGGER.debug("\t" + i + ". " + pjp.getArgs()[i]);
            }
        }
        LOGGER.debug("方法 " + className + "." + methodName + " 开始计时...");
        long firstTimeStamp = System.currentTimeMillis();
        LOGGER.debug("获得当前系统时间戳 " + firstTimeStamp);
        try {
            LOGGER.debug("原始方法执行");
            result = pjp.proceed(pjp.getArgs());
            return result;
        } catch (Throwable e) {
            LOGGER.debug("方法 " + className + "." + methodName + " 抛出异常", e);
            throw e;
        } finally {
            LOGGER.debug("原始方法执行结束，返回的对象为 " + result);
            long lastTimeStamp = System.currentTimeMillis();
            LOGGER.debug("获得当前系统时间戳 " + lastTimeStamp);
            LOGGER.debug("计算方法执行时间，公式: " + lastTimeStamp + "-" + firstTimeStamp);
            LOGGER.debug("方法 " + className + "." + methodName + " 运行结束，用时 " + (lastTimeStamp - firstTimeStamp) + " 毫秒");
        }
    }


}
