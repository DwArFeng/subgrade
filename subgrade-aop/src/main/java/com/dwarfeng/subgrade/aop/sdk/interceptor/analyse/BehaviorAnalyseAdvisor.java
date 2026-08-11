package com.dwarfeng.subgrade.aop.sdk.interceptor.analyse;

import com.dwarfeng.subgrade.aop.sdk.configuration.SystemPropertyConstants;
import com.dwarfeng.subgrade.aop.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.basic.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.basic.stack.log.SingleLevelLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
     * 日志记录器缓存。
     *
     * <p>
     * 使用 {@link ClassValue} 按日志记录器类隔离缓存，避免静态 Map 直接持有类对象导致动态类加载场景下的类卸载受阻。
     *
     * @since 1.2.0
     */
    private static final ClassValue<Map<LogLevel, SingleLevelLogger>> CACHED_LOGGER_CLASS_VALUE =
            new ClassValue<>() {
                @Override
                protected Map<LogLevel, SingleLevelLogger> computeValue(@NotNull Class<?> type) {
                    return SingleLevelLoggerFactory.newInstanceMap(LoggerFactory.getLogger(type));
                }
            };

    /**
     * 行为分析元数据缓存。
     *
     * <p>
     * 缓存以目标类为生命周期边界，每个目标类下再按方法签名缓存直接方法、注解、日志类和 SkipRecord 解析结果。
     * 该方案减少切面高频调用时的反射解析开销，同时不把不同类加载器中的同名类混入同一个全局字符串键空间。
     *
     * @since 1.8.3
     */
    private static final ClassValue<ConcurrentMap<MethodCacheKey, BehaviorAnalyseMetadata>>
            CACHED_METADATA_CLASS_VALUE = new ClassValue<>() {
        @Override
        protected ConcurrentMap<MethodCacheKey, BehaviorAnalyseMetadata> computeValue(@NotNull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private static final String AROUND_VALUE =
            "@annotation(com.dwarfeng.subgrade.aop.sdk.interceptor.analyse.BehaviorAnalyse) ||" +
                    " @within(com.dwarfeng.subgrade.aop.sdk.interceptor.analyse.BehaviorAnalyse)";

    private static final boolean DETAILED_BEHAVIOR_ANALYSE_LOG = Boolean.parseBoolean(
            System.getProperty(SystemPropertyConstants.VALUE_DETAILED_BEHAVIOR_ANALYSE_LOG, "false")
    );

    private static final boolean BEHAVIOR_ANALYSE_METADATA_CACHE_ENABLED = Boolean.parseBoolean(
            System.getProperty(SystemPropertyConstants.VALUE_BEHAVIOR_ANALYSE_METADATA_CACHE_ENABLED, "true")
    );

    @Around(AROUND_VALUE)
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获取行为分析元数据，缓存开启时复用解析结果，关闭时保持旧有逐次解析路径。
        BehaviorAnalyseMetadata metadata = metadata(pjp);

        // 获取日志记录器。
        Map<LogLevel, SingleLevelLogger> loggerLevelLoggerMap = CACHED_LOGGER_CLASS_VALUE.get(
                metadata.loggerClass()
        );
        SingleLevelLogger logger = loggerLevelLoggerMap.get(metadata.logLevel());

        // 获取 SkipRecord 注解解析结果。
        boolean skipResultRecord = metadata.skipResultRecord();
        boolean[] skipParamRecord = metadata.skipParamRecord();
        Object[] args = pjp.getArgs();
        if (skipParamRecord.length != args.length) {
            throw new IllegalStateException("行为分析元数据中的参数数量与运行时参数数量不一致");
        }

        // 定义原始方法执行的结果和异常。
        Object result = null;
        Throwable throwable = null;

        // 行为分析开始并记录日志。
        logger.log("行为分析开始...");
        String fullMethodName = metadata.fullMethodName();
        logger.log("方法执行开始: {}...", fullMethodName);
        if (args.length == 0) {
            logger.log("  参数: 无");
        } else {
            for (int i = 0; i < args.length; i++) {
                if (skipParamRecord[i]) {
                    logger.log("  参数 {}/{}: SkipRecord 注解生效, 不记录此参数", i, args.length);
                } else {
                    logger.log("  参数 {}/{}: {}", i, args.length, smartToString(args[i]));
                }
            }
        }
        mayDetailedLog(logger, "开始计时: {}...", fullMethodName);
        long firstTimeStamp = System.currentTimeMillis();
        mayDetailedLog(logger, "获取当前系统时间戳: {}", firstTimeStamp);

        // 执行原始方法。
        try {
            mayDetailedLog(logger, "调用原始方法: {}...", fullMethodName);
            result = pjp.proceed(args);
        } catch (Throwable t) {
            throwable = t;
        }

        // 计算方法执行时间。
        mayDetailedLog(logger, "结束计时: {}...", fullMethodName);
        long lastTimeStamp = System.currentTimeMillis();
        long timeCost = lastTimeStamp - firstTimeStamp;
        mayDetailedLog(logger, "获取当前系统时间戳: {}", lastTimeStamp);
        mayDetailedLog(
                logger, "计算方法执行时间: {} - {} = {} 毫秒", lastTimeStamp, firstTimeStamp, timeCost
        );

        // 根据 result 和 throwable 按条件记录日志。
        logger.log("方法执行结束: {}", fullMethodName);
        if (Objects.nonNull(throwable)) {
            logger.log("  抛出异常: ", throwable);
        } else {
            if (skipResultRecord) {
                logger.log("  返回对象: SkipRecord 注解生效, 不记录返回对象");
            } else {
                logger.log("  返回对象: {}", smartToString(result));
            }
        }
        logger.log("  用时: {} 毫秒", timeCost);

        // 行为分析结束并记录日志。
        if (Objects.nonNull(throwable)) {
            logger.log("行为分析结束, 方法: {}, 用时: {} 毫秒, 抛出异常", fullMethodName, timeCost);
        } else {
            logger.log("行为分析结束, 方法: {}, 用时: {} 毫秒, 正常返回", fullMethodName, timeCost);
        }

        // 根据 result 和 throwable 按条件返回结果或抛出异常。
        if (Objects.nonNull(throwable)) {
            throw throwable;
        } else {
            return result;
        }
    }

    private BehaviorAnalyseMetadata metadata(ProceedingJoinPoint pjp) {
        if (!BEHAVIOR_ANALYSE_METADATA_CACHE_ENABLED) {
            return parseMetadata(pjp);
        }
        Class<?> targetClass = AdvisorUtil.directClass(pjp);
        MethodCacheKey cacheKey = MethodCacheKey.of(pjp);
        ConcurrentMap<MethodCacheKey, BehaviorAnalyseMetadata> metadataMap = CACHED_METADATA_CLASS_VALUE.get(
                targetClass
        );
        return metadataMap.computeIfAbsent(cacheKey, k -> parseMetadata(pjp));
    }

    private BehaviorAnalyseMetadata parseMetadata(ProceedingJoinPoint pjp) {
        Method method = AdvisorUtil.directMethod(pjp);
        Class<?> targetClass = AdvisorUtil.directClass(pjp);

        // 获取方法中或类中的 BehaviorAnalyse 注解, 这个步骤可以保证获取非 null 的注解值。
        BehaviorAnalyse behaviorAnalyse = method.getAnnotation(BehaviorAnalyse.class);
        if (Objects.isNull(behaviorAnalyse)) {
            behaviorAnalyse = targetClass.getAnnotation(BehaviorAnalyse.class);
        }
        assert behaviorAnalyse != null;

        // 获取日志记录器的类。
        Class<?> loggerClass = behaviorAnalyse.loggerClass();
        if (Objects.equals(Void.class, loggerClass)) {
            loggerClass = targetClass;
        }

        // 分析方法和参数中有无 SkipRecord 注解。
        boolean skipResultRecord = Objects.nonNull(method.getAnnotation(SkipRecord.class));
        boolean[] skipParamRecord = new boolean[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            skipParamRecord[i] = Objects.nonNull(method.getParameters()[i].getAnnotation(SkipRecord.class));
        }

        String className = targetClass.getCanonicalName();
        String fullMethodName = className + "." + method.getName();

        return new BehaviorAnalyseMetadata(
                method, behaviorAnalyse, loggerClass, behaviorAnalyse.logLevel(), fullMethodName,
                skipResultRecord, skipParamRecord
        );
    }

    /**
     * 在详细行为分析日志开启时输出一条完整消息（无占位符）。
     *
     * @param logger  单级日志记录器
     * @param message 日志正文
     */
    // mayDetailedLog 重载方法，用于加速 mayDetailedLog 的调用，避免不必要的字符串拼接开销，故忽略相关警告。
    @SuppressWarnings({"SameParameterValue", "unused"})
    private void mayDetailedLog(SingleLevelLogger logger, String message) {
        if (!DETAILED_BEHAVIOR_ANALYSE_LOG) {
            return;
        }
        logger.log(message);
    }

    /**
     * 在详细行为分析日志开启时输出一条占位符格式的日志；占位符语义与底层 SLF4J 一致。
     *
     * @param logger 单级日志记录器
     * @param format 消息格式串（含 <code>{}</code> 占位）
     * @param arg    填充第一个占位的参数
     */
    // mayDetailedLog 重载方法，用于加速 mayDetailedLog 的调用，避免不必要的字符串拼接开销，故忽略相关警告。
    @SuppressWarnings({"SameParameterValue", "unused"})
    private void mayDetailedLog(SingleLevelLogger logger, String format, Object arg) {
        if (!DETAILED_BEHAVIOR_ANALYSE_LOG) {
            return;
        }
        logger.log(format, arg);
    }

    /**
     * 在详细行为分析日志开启时输出一条占位符格式的日志；占位符语义与底层 SLF4J 一致。
     *
     * @param logger 单级日志记录器
     * @param format 消息格式串（含 <code>{}</code> 占位）
     * @param arg1   填充占位用的第一个参数
     * @param arg2   填充占位用的第二个参数
     */
    // mayDetailedLog 重载方法，用于加速 mayDetailedLog 的调用，避免不必要的字符串拼接开销，故忽略相关警告。
    @SuppressWarnings({"SameParameterValue", "unused"})
    private void mayDetailedLog(SingleLevelLogger logger, String format, Object arg1, Object arg2) {
        if (!DETAILED_BEHAVIOR_ANALYSE_LOG) {
            return;
        }
        logger.log(format, arg1, arg2);
    }

    /**
     * 在详细行为分析日志开启时输出一条占位符格式的日志；占位符语义与底层 SLF4J 一致。
     *
     * @param logger    单级日志记录器
     * @param format    消息格式串（含 <code>{}</code> 占位）
     * @param arguments 依次填充占位的参数
     */
    @SuppressWarnings({"SameParameterValue", "unused"})
    private void mayDetailedLog(SingleLevelLogger logger, String format, Object... arguments) {
        if (!DETAILED_BEHAVIOR_ANALYSE_LOG) {
            return;
        }
        logger.log(format, arguments);
    }

    private String smartToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj.getClass().isArray()) {
            switch (obj) {
                case Object[] objects -> {
                    return Arrays.toString(objects);
                }
                case int[] ints -> {
                    return Arrays.toString(ints);
                }
                case long[] longs -> {
                    return Arrays.toString(longs);
                }
                case short[] shorts -> {
                    return Arrays.toString(shorts);
                }
                case byte[] bytes -> {
                    return Arrays.toString(bytes);
                }
                case char[] chars -> {
                    return Arrays.toString(chars);
                }
                case double[] doubles -> {
                    return Arrays.toString(doubles);
                }
                case float[] floats -> {
                    return Arrays.toString(floats);
                }
                case boolean[] booleans -> {
                    return Arrays.toString(booleans);
                }
                default -> {
                }
            }
        }
        return Objects.toString(obj);
    }

    /**
     * 方法缓存键。
     *
     * <p>
     * 该对象仅描述目标类内部的方法签名，不包含目标类名称字符串。实际缓存由 {@link ClassValue} 先按目标类隔离，
     * 再通过该键区分重载方法，避免不同类加载器中的同名类共用缓存项。
     *
     * @author DwArFeng
     * @since 1.8.3
     */
    private record MethodCacheKey(String methodName, Class<?>[] parameterTypes) {

        private static MethodCacheKey of(ProceedingJoinPoint pjp) {
            Method method = AdvisorUtil.methodSignature(pjp).getMethod();
            return new MethodCacheKey(method.getName(), method.getParameterTypes());
        }
    }

    /**
     * 行为分析元数据。
     *
     * <p>
     * 该对象聚合行为分析切面执行前需要反射解析的稳定信息，缓存开启时会被复用，从而减少直接方法、注解和参数注解的重复解析。
     *
     * @author DwArFeng
     * @since 1.8.3
     */
    private record BehaviorAnalyseMetadata(
            Method method, BehaviorAnalyse behaviorAnalyse, Class<?> loggerClass, LogLevel logLevel,
            String fullMethodName, boolean skipResultRecord, boolean[] skipParamRecord
    ) {
    }
}
