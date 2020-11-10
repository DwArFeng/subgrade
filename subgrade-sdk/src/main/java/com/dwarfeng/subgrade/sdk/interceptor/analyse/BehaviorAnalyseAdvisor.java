package com.dwarfeng.subgrade.sdk.interceptor.analyse;

import com.dwarfeng.dutil.basic.cna.ArrayUtil;
import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.log.SingleLevelLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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

    /**
     * @since 1.2.0
     */
    private static final Map<LogLevel, SingleLevelLogger> logger_MAP =
            SingleLevelLoggerFactory.newInstanceMap(LoggerFactory.getLogger(BehaviorAnalyseAdvisor.class));

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse) || @within(com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获取方法中或类中的 BehaviorAnalyse 注解，这个步骤可以保证获取非 null 的注解值。
        BehaviorAnalyse behaviorAnalyse;
        behaviorAnalyse = AdvisorUtil.directMethod(pjp).getAnnotation(BehaviorAnalyse.class);
        if (Objects.isNull(behaviorAnalyse)) {
            behaviorAnalyse = pjp.getTarget().getClass().getAnnotation(BehaviorAnalyse.class);
        }
        assert behaviorAnalyse != null;
        SingleLevelLogger logger = logger_MAP.get(behaviorAnalyse.logLevel());
        // 分析方法和参数中有无 SkipRecord 注解。
        boolean skipResultRecord = Objects.nonNull(AdvisorUtil.directMethodAnnotation(pjp, SkipRecord.class));
        boolean[] skipParamRecord = ArrayUtil.unpack(
                Arrays.stream(AdvisorUtil.directParameterAnnotation(pjp, SkipRecord.class))
                        .map(Objects::nonNull).toArray(Boolean[]::new)
        );
        // 进行行为分析。
        logger.log("进入增强方法");
        String className = AdvisorUtil.directClass(pjp).getCanonicalName();
        String methodName = AdvisorUtil.directMethod(pjp).getName();
        Object result = null;
        logger.log("获得方法名称 " + methodName);
        logger.log("获得方法参数");
        if (pjp.getArgs().length == 0) {
            logger.log("\t没有参数");
        } else {
            for (int i = 0; i < pjp.getArgs().length; i++) {
                if (skipParamRecord[i]) {
                    logger.log("\t" + i + ". SkipRecord 注解生效，不记录指定参数");
                } else {
                    logger.log("\t" + i + ". " + pjp.getArgs()[i]);
                }
            }
        }
        logger.log("方法 " + className + "." + methodName + " 开始计时...");
        long firstTimeStamp = System.currentTimeMillis();
        logger.log("获得当前系统时间戳 " + firstTimeStamp);
        try {
            logger.log("原始方法执行");
            result = pjp.proceed(pjp.getArgs());
            return result;
        } catch (Throwable e) {
            logger.log("方法 " + className + "." + methodName + " 抛出异常", e);
            throw e;
        } finally {
            if (skipResultRecord) {
                logger.log("原始方法执行结束，SkipRecord 注解生效，不记录返回对象");
            } else {
                logger.log("原始方法执行结束，返回的对象为 " + result);
            }
            long lastTimeStamp = System.currentTimeMillis();
            logger.log("获得当前系统时间戳 " + lastTimeStamp);
            logger.log("计算方法执行时间，公式: " + lastTimeStamp + "-" + firstTimeStamp);
            logger.log("方法 " + className + "." + methodName + " 运行结束，用时 " + (lastTimeStamp - firstTimeStamp) + " 毫秒");
        }
    }
}
