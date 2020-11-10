package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 友好性增强。
 *
 * <p>
 * 友好性增强用于对输入的参数以及返回的结果做出微小的调整，以针对访问者进行“友好”的输入以及输出。
 * <br>
 * 常见的“友好”包括分页数据从1开始计数等等。
 *
 * <p>
 * 使用友好性接口，需要在方法或实现类中增加 {@link Friendly} 注解。如果注解增加在方法上，则针对该方法本身进行友好性增强；
 * 如果注解增加在实现类上，则对类中的所有公共方法进行友好性增强。
 * <br>
 * 如果实现类和公共方法都有  {@link Friendly} 注解，则同时启用两方面的注解，在优先级上，实现类中的注解优先于方法的注解。
 *
 * <p>
 * 您可以使用多个 {@link Friendly} 注解，如以下例子。
 * <blockquote><pre>
 * &#64;Friendly(paramManger = "xxx", resultManger = "xxx", optionalKey = "a")
 * &#64;Friendly(paramManger = "xxx", resultManger = "xxx", optionalKey = "b")
 * public class FriendlyClass {
 *
 *     &#64;Friendly(paramManger = "xxx", resultManger = "xxx", optionalKey = "c")
 *     &#64;Friendly(paramManger = "xxx", resultManger = "xxx", optionalKey = "d")
 *     public XXX friendlyMethod(SomeParam someParam){
 *         ...
 *         return new XXX();
 *     }
 * }
 * </pre></blockquote>
 *
 * <p>
 * 假设所有的注解都被启动了，那么按照 {@link Friendly} 注解规则，这些注解与切入点方法的执行顺序是：
 * <blockquote><pre>
 * 1. optionalKey = "a" 的注解的 paramManger 执行
 * 2. optionalKey = "b" 的注解的 paramManger 执行
 * 3. optionalKey = "c" 的注解的 paramManger 执行
 * 4. optionalKey = "d" 的注解的 paramManger 执行
 * 5. 原始方法执行
 * 6. optionalKey = "d" 的注解的 resultManger 执行
 * 7. optionalKey = "c" 的注解的 resultManger 执行
 * 8. optionalKey = "b" 的注解的 resultManger 执行
 * 9. optionalKey = "a" 的注解的 resultManger 执行
 * </pre></blockquote>
 *
 * <p>
 * 友好性增强默认是不启用的，您需在java运行环境中增加环境参数以启动友好性注解。
 * <blockquote><pre>
 * 1. 增加运行参数 -Dsubgrade.friendly.enable 以启用所有的 {@link Friendly} 注解。
 * 2. 增加运行参数 -Dsubgrade.friendly.enable.a 以启用所有 optionalKey = "a" 的 {@link Friendly} 注解。
 * </pre></blockquote>
 *
 * @author DwArFeng
 * @since 1.0.2
 */
@Component
@Aspect
public class FriendlyAdvisor implements ApplicationContextAware, EmbeddedValueResolverAware {

    /**
     * 程序配置中，启用所有友好性增强的键。
     */
    public static final String KEY_ENABLE_ALL = "subgrade.friendly.enable";
    /**
     * 程序配置中，启用特定的友好性增强的键的格式化字符串。
     */
    public static final String FORMAT_KEY_ENABLE_SPECIFIC = "subgrade.friendly.enable.%s";

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendlyAdvisor.class);

    private ApplicationContext applicationContext;
    @Nullable
    private StringValueResolver embeddedValueResolver;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyList) || " +
            "@annotation(com.dwarfeng.subgrade.sdk.interceptor.friendly.Friendly) || " +
            "@within(com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyList) || " +
            "@within(com.dwarfeng.subgrade.sdk.interceptor.friendly.Friendly)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        LOGGER.debug("方法 " + className + "." + methodName + " 切入友好性AOP...");

        //扫描方法中或类中的 @Friendly 注解。
        List<Friendly> friendlies = scanFriendlyList(pjp);
        //获取有效的 @Friendly 注解。
        friendlies = scanEnabledFriendlies(friendlies);

        //如果有效的 @Friendly 注解为空，则直接返回原始方法，不进行增强。
        if (friendlies.isEmpty()) {
            return pjp.proceed(pjp.getArgs());
        }

        //获取方法执行的原始参数。
        Object[] param = pjp.getArgs();

        //按顺序（正向）调用 friendlies 中的增强。
        for (Friendly friendly : friendlies) {
            if (!friendly.paramManger().isEmpty()) {
                FriendlyParamAopManager friendlyParamAopManager = applicationContext.getBean(
                        friendly.paramManger(), FriendlyParamAopManager.class);
                param = friendlyParamAopManager.processParam(pjp, pjp.getArgs());
            }
        }

        //执行友好化过后的参数，获得返回的结果。
        Object result = pjp.proceed(param);

        //按顺序（逆向）调用 friendlies 中的增强。
        Collections.reverse(friendlies);
        for (Friendly friendly : friendlies) {
            if (!friendly.resultManger().isEmpty()) {
                FriendlyResultAopManager friendlyResultAopManager = applicationContext.getBean(
                        friendly.resultManger(), FriendlyResultAopManager.class);
                result = friendlyResultAopManager.processResult(pjp, result);
            }
        }

        //返回结果。
        return result;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nullable
    public StringValueResolver getEmbeddedValueResolver() {
        return embeddedValueResolver;
    }

    @Override
    public void setEmbeddedValueResolver(@Nonnull StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    private List<Friendly> scanFriendlyList(ProceedingJoinPoint pjp) {
        List<Friendly> friendlies = new ArrayList<>();
        //获取类中的 @FriendlyList 注解。
        LOGGER.debug("扫描方类中的 @FriendlyList 注解...");
        FriendlyList friendlyList = AdvisorUtil.directClassAnnotation(pjp, FriendlyList.class);
        if (Objects.nonNull(friendlyList)) {
            LOGGER.debug("类中存在 @FriendlyList 注解...");
            friendlies.addAll(Arrays.asList(friendlyList.value()));
        } else {
            LOGGER.debug("类中不存在 @FriendlyList 注解，继续扫描方类中的 @Friendly 注解...");
            friendlies.addAll(Arrays.asList(AdvisorUtil.directClassAnnotations(pjp, Friendly.class)));
        }
        //获取方法中的 @FriendlyList 注解。
        LOGGER.debug("扫描方法中的 @FriendlyList 注解...");
        friendlyList = AdvisorUtil.directMethodAnnotation(pjp, FriendlyList.class);
        if (Objects.nonNull(friendlyList)) {
            LOGGER.debug("方法中存在 @FriendlyList 注解...");
            friendlies.addAll(Arrays.asList(friendlyList.value()));
        } else {
            LOGGER.debug("方法中存在 @FriendlyList 注解，继续扫描方法中的 @Friendly 注解...");
            friendlies.addAll(Arrays.asList(AdvisorUtil.directMethodAnnotations(pjp, Friendly.class)));
        }
        return friendlies;
    }

    private List<Friendly> scanEnabledFriendlies(List<Friendly> friendlies) {
        if (System.getProperties().containsKey(KEY_ENABLE_ALL)) {
            return friendlies;
        }

        return friendlies.stream().filter(f -> {
            String optionalKey = f.optionalKey();
            if (optionalKey.isEmpty()) {
                return false;
            }
            if (Objects.nonNull(embeddedValueResolver)) {
                optionalKey = embeddedValueResolver.resolveStringValue(optionalKey);
            }
            return System.getProperties().containsKey(String.format(FORMAT_KEY_ENABLE_SPECIFIC, optionalKey));
        }).collect(Collectors.toList());
    }
}
