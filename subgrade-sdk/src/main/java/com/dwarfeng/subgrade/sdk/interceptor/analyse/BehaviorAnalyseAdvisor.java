package com.dwarfeng.subgrade.sdk.interceptor.analyse;

import com.dwarfeng.dutil.basic.cna.ArrayUtil;
import com.dwarfeng.subgrade.sdk.SystemPropertyConstants;
import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.log.SingleLevelLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 方法行为分析增强。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
@Component
@Aspect
/*
 * 该 AOP 需要在几乎所有的 AOP 之前执行，因为它需要记录方法的执行时间。
 * 但是根据 Spring 的文档说明：AOP 调用链中，ExposeInvocationInterceptor 应该是最先执行的，
 * 因为它需要在调用链中暴露出当前的 MethodInvocation。
 * ExposeInvocationInterceptor Javadoc 原文如下：
 *   If used, this interceptor will normally be the first in the interceptor chain.
 * 因此，该 AOP 的优先级应该设置为最高，但必须低于 ExposeInvocationInterceptor 的优先级。
 * 根据 ExposeInvocationInterceptor 的源码，ExposeInvocationInterceptor 的优先级是：
 *   PriorityOrdered.HIGHEST_PRECEDENCE + 1;
 * 因此，该 AOP 的优先级应该设置为：
 *   PriorityOrdered.HIGHEST_PRECEDENCE + 10;
 * 以保证该 AOP 在 ExposeInvocationInterceptor 之后执行。
 * 2-9 保留给其它的 AOP。
 */
@Order(PriorityOrdered.HIGHEST_PRECEDENCE + 10)
public class BehaviorAnalyseAdvisor {

    /**
     * @since 1.2.0
     */
    private static final Map<Class<?>, Map<LogLevel, SingleLevelLogger>> CACHED_LOGGER_CLASS_LOGGER_MAP =
            Collections.synchronizedMap(new HashMap<>());

    private static final String AROUND_VALUE =
            "@annotation(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse) ||" +
                    " @within(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse)";

    private static final boolean DETAILED_BEHAVIOR_ANALYSE_LOG = Boolean.parseBoolean(
            System.getProperty(SystemPropertyConstants.VALUE_DETAILED_BEHAVIOR_ANALYSE_LOG, "false")
    );

    @Around(AROUND_VALUE)
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获取方法中或类中的 BehaviorAnalyse 注解, 这个步骤可以保证获取非 null 的注解值。
        BehaviorAnalyse behaviorAnalyse;
        behaviorAnalyse = AdvisorUtil.directMethod(pjp).getAnnotation(BehaviorAnalyse.class);
        if (Objects.isNull(behaviorAnalyse)) {
            behaviorAnalyse = pjp.getTarget().getClass().getAnnotation(BehaviorAnalyse.class);
        }
        assert behaviorAnalyse != null;

        // 获取日志记录器的类。
        Class<?> loggerClass = behaviorAnalyse.loggerClass();
        if (Objects.equals(Void.class, loggerClass)) {
            loggerClass = pjp.getTarget().getClass();
        }

        // 获取日志记录器。
        Map<LogLevel, SingleLevelLogger> loggerLevelLoggerMap = CACHED_LOGGER_CLASS_LOGGER_MAP.computeIfAbsent(
                loggerClass, k -> SingleLevelLoggerFactory.newInstanceMap(LoggerFactory.getLogger(k))
        );
        SingleLevelLogger logger = loggerLevelLoggerMap.get(behaviorAnalyse.logLevel());

        // 分析方法和参数中有无 SkipRecord 注解。
        boolean skipResultRecord = Objects.nonNull(AdvisorUtil.directMethodAnnotation(pjp, SkipRecord.class));
        boolean[] skipParamRecord = ArrayUtil.unpack(
                Arrays.stream(AdvisorUtil.directParameterAnnotation(pjp, SkipRecord.class))
                        .map(Objects::nonNull).toArray(Boolean[]::new)
        );

        // 定义原始方法执行的结果和异常。
        Object result = null;
        Throwable throwable = null;

        // 行为分析开始并记录日志。
        logger.log("行为分析开始...");
        String className = AdvisorUtil.directClass(pjp).getCanonicalName();
        String methodName = AdvisorUtil.directMethod(pjp).getName();
        String fullMethodName = className + "." + methodName;
        logger.log("方法执行开始: " + fullMethodName + "...");
        if (pjp.getArgs().length == 0) {
            logger.log("  参数: 无");
        } else {
            for (int i = 0; i < pjp.getArgs().length; i++) {
                if (skipParamRecord[i]) {
                    logger.log("  参数 " + i + "/" + pjp.getArgs().length + ": SkipRecord 注解生效, 不记录此参数");
                } else {
                    logger.log("  参数 " + i + "/" + pjp.getArgs().length + ": " + smartToString(pjp.getArgs()[i]));
                }
            }
        }
        mayDetailedLog(logger, "开始计时: " + fullMethodName + "...");
        long firstTimeStamp = System.currentTimeMillis();
        mayDetailedLog(logger, "获取当前系统时间戳: " + firstTimeStamp);

        // 执行原始方法。
        try {
            mayDetailedLog(logger, "调用原始方法: " + fullMethodName + "...");
            result = pjp.proceed(pjp.getArgs());
        } catch (Throwable t) {
            throwable = t;
        }

        // 计算方法执行时间。
        mayDetailedLog(logger, "结束计时: " + fullMethodName + "...");
        long lastTimeStamp = System.currentTimeMillis();
        long timeCost = lastTimeStamp - firstTimeStamp;
        mayDetailedLog(logger, "获取当前系统时间戳: " + lastTimeStamp);
        mayDetailedLog(
                logger, "计算方法执行时间: " + lastTimeStamp + " - " + firstTimeStamp + " = " + timeCost + " 毫秒"
        );

        // 根据 result 和 throwable 按条件记录日志。
        logger.log("方法执行结束: " + fullMethodName);
        if (Objects.nonNull(throwable)) {
            logger.log("  抛出异常: ", throwable);
        } else {
            if (skipResultRecord) {
                logger.log("  返回对象: SkipRecord 注解生效, 不记录返回对象");
            } else {
                logger.log("  返回对象: " + smartToString(result));
            }
        }
        logger.log("  用时: " + timeCost + " 毫秒");

        // 行为分析结束并记录日志。
        if (Objects.nonNull(throwable)) {
            logger.log("行为分析结束, 方法: " + fullMethodName + ", 用时: " + timeCost + " 毫秒, 抛出异常");
        } else {
            logger.log("行为分析结束, 方法: " + fullMethodName + ", 用时: " + timeCost + " 毫秒, 正常返回");
        }

        // 根据 result 和 throwable 按条件返回结果或抛出异常。
        if (Objects.nonNull(throwable)) {
            throw throwable;
        } else {
            return result;
        }
    }

    private void mayDetailedLog(SingleLevelLogger logger, String message) {
        if (!DETAILED_BEHAVIOR_ANALYSE_LOG) {
            return;
        }
        logger.log(message);
    }

    private String smartToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return Arrays.toString((Object[]) obj);
            } else if (obj instanceof int[]) {
                return Arrays.toString((int[]) obj);
            } else if (obj instanceof long[]) {
                return Arrays.toString((long[]) obj);
            } else if (obj instanceof short[]) {
                return Arrays.toString((short[]) obj);
            } else if (obj instanceof byte[]) {
                return Arrays.toString((byte[]) obj);
            } else if (obj instanceof char[]) {
                return Arrays.toString((char[]) obj);
            } else if (obj instanceof double[]) {
                return Arrays.toString((double[]) obj);
            } else if (obj instanceof float[]) {
                return Arrays.toString((float[]) obj);
            } else if (obj instanceof boolean[]) {
                return Arrays.toString((boolean[]) obj);
            }
        }
        return Objects.toString(obj);
    }
}
