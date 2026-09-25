package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 通用方法贡献器。
 *
 * <p>
 * 该贡献器用于 {@link com.dwarfeng.subgrade.sdk.interceptor.audit.AuditInvocationContribute}，
 * 不读取源对象，而是通过 {@link AuditRecordContext} 解析整次方法调用。
 * 它按照配置的 {@link Rule} 顺序生成审计字段，默认不配置任何规则。
 * 规则返回 <code>null</code> 时不生成对应的审计字段，因此空参数、空返回值和空异常消息会被跳过。
 * 非空结果保持解析器返回的原始类型。
 *
 * <p>
 * 目标类和方法通过 {@link AdvisorUtil} 解析为被代理对象的直接类和直接方法。
 * 该贡献器不把未配置的参数或返回值写入审计映射，也不负责类型转换、敏感信息识别、脱敏或截断。
 * 应用应根据审计需要配置规则，或使用专用的 {@link Contributor} 处理敏感信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class GeneralMethodContributor implements Contributor {

    private List<Rule> rules = Collections.emptyList();

    public GeneralMethodContributor() {
    }

    public GeneralMethodContributor(List<Rule> rules) {
        setRules(rules);
    }

    @Override
    @Nonnull
    public Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception {
        if (source != null) {
            throw new IllegalArgumentException("方法贡献器没有源对象");
        }
        requireJoinPoint(context);

        Map<String, Object> properties = new LinkedHashMap<>();
        for (Rule rule : rules) {
            Object value = rule.getResolver().resolve(context);
            if (value == null) {
                continue;
            }
            String key = rule.getKey();
            if (properties.containsKey(key)) {
                throw new IllegalStateException("方法审计键重复: " + key);
            }
            properties.put(key, value);
        }
        return properties;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("方法规则列表不能为 null");
        }

        List<Rule> copiedRules = new ArrayList<>(rules.size());
        Set<String> keys = new HashSet<>();
        for (Rule rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("方法规则列表不能包含 null 元素");
            }
            if (!keys.add(rule.getKey())) {
                throw new IllegalArgumentException("方法审计键重复: " + rule.getKey());
            }
            copiedRules.add(rule);
        }
        this.rules = Collections.unmodifiableList(copiedRules);
    }

    @Override
    public String toString() {
        return "GeneralMethodContributor{" +
                "rules=" + rules +
                '}';
    }

    private static ProceedingJoinPoint requireJoinPoint(AuditRecordContext context) {
        ProceedingJoinPoint joinPoint = context.getPjp();
        if (joinPoint == null) {
            throw new IllegalStateException("方法调用连接点不能为 null");
        }
        return joinPoint;
    }

    /**
     * 解析器。
     *
     * <p>
     * 该解析器从一次方法调用的 {@link AuditRecordContext} 中解析单个审计字段的原始值。
     * 返回值保持原始类型；返回 <code>null</code> 表示不生成对应的审计字段。
     */
    @FunctionalInterface
    public interface Resolver {

        /**
         * 解析方法调用字段。
         *
         * @param context 审计记录上下文。
         * @return 解析得到的原始值，或 <code>null</code>。
         * @throws Exception 解析过程中发生的任何异常。
         */
        @Nullable
        Object resolve(@Nonnull AuditRecordContext context) throws Exception;
    }

    /**
     * 规则。
     *
     * <p>
     * 该规则将一个审计键与一个 {@link Resolver} 绑定。
     * 可通过通用工厂定义任意取值逻辑，也可通过专用工厂读取目标类、方法、参数、返回值或异常信息。
     */
    public static final class Rule {

        private final String key;
        private final Resolver resolver;

        private Rule(String key, Resolver resolver) {
            if (key == null || key.trim().isEmpty()) {
                throw new IllegalArgumentException("审计键不能为空");
            }
            if (resolver == null) {
                throw new IllegalArgumentException("解析器不能为 null");
            }
            this.key = key;
            this.resolver = resolver;
        }

        /**
         * 创建通用规则。
         *
         * @param key      审计键。
         * @param resolver 解析器。
         * @return 创建的规则。
         */
        public static Rule of(String key, Resolver resolver) {
            return new Rule(key, resolver);
        }

        /**
         * 创建目标类名规则。
         *
         * <p>
         * 类名取被代理对象的直接类，使用 {@link Class#getName()}。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule className(String key) {
            return of(key, context -> targetClass(requireJoinPoint(context)).getName());
        }

        /**
         * 创建方法名规则。
         *
         * <p>
         * 方法名取被代理对象的直接方法。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule methodName(String key) {
            return of(key, context -> AdvisorUtil.directMethod(requireJoinPoint(context)).getName());
        }

        /**
         * 创建执行结果规则。
         *
         * <p>
         * 方法正常返回时值为 {@link Boolean#TRUE}，异常结束时值为 {@link Boolean#FALSE}。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule success(String key) {
            return of(key, context -> context.isSuccess() ? Boolean.TRUE : Boolean.FALSE);
        }

        /**
         * 创建单个参数规则。
         *
         * <p>
         * 返回指定下标参数的原始值。
         * 参数值为 <code>null</code> 时不生成审计字段。
         *
         * @param key   审计键。
         * @param index 参数下标，从 0 开始。
         * @return 创建的规则。
         */
        public static Rule argument(String key, int index) {
            if (index < 0) {
                throw new IllegalArgumentException("方法参数下标不能小于 0");
            }
            return of(key, context -> argumentAt(requireJoinPoint(context), index));
        }

        /**
         * 创建单个参数规则。
         *
         * <p>
         * 按直接方法的参数名定位参数，并返回该参数的原始值。
         * 参数值为 <code>null</code> 时不生成审计字段。
         *
         * @param key           审计键。
         * @param parameterName 参数名称。
         * @return 创建的规则。
         */
        public static Rule argument(String key, String parameterName) {
            if (parameterName == null || parameterName.trim().isEmpty()) {
                throw new IllegalArgumentException("方法参数名称不能为空");
            }
            return of(key, context -> argumentNamed(requireJoinPoint(context), parameterName));
        }

        /**
         * 创建全部参数规则。
         *
         * <p>
         * 参数数组使用 {@link Arrays#deepToString(Object[])} 转换为字符串。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule arguments(String key) {
            return of(key, context -> {
                Object[] arguments = requireJoinPoint(context).getArgs();
                if (arguments == null) {
                    throw new IllegalStateException("方法参数不能为 null");
                }
                return Arrays.deepToString(arguments);
            });
        }

        /**
         * 创建返回值规则。
         *
         * <p>
         * 返回方法的原始返回值。
         * 返回值为 <code>null</code> 或方法异常结束时不生成审计字段。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule result(String key) {
            return of(key, AuditRecordContext::getResult);
        }

        /**
         * 创建异常类型规则。
         *
         * <p>
         * 方法正常返回时不生成审计字段。异常结束时记录异常类的 {@link Class#getName()}。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule exceptionType(String key) {
            return of(key, context -> {
                Throwable throwable = context.getThrowable();
                return throwable == null ? null : throwable.getClass().getName();
            });
        }

        /**
         * 创建异常消息规则。
         *
         * <p>
         * 方法正常返回，或异常消息为 <code>null</code> 时，不生成审计字段。
         *
         * @param key 审计键。
         * @return 创建的规则。
         */
        public static Rule exceptionMessage(String key) {
            return of(key, context -> {
                Throwable throwable = context.getThrowable();
                return throwable == null ? null : throwable.getMessage();
            });
        }

        private static Class<?> targetClass(ProceedingJoinPoint joinPoint) {
            Object target = joinPoint.getTarget();
            if (target == null) {
                throw new IllegalStateException("方法调用目标不能为 null");
            }
            return target.getClass();
        }

        private static Object argumentAt(ProceedingJoinPoint joinPoint, int index) {
            Object[] arguments = requireArguments(joinPoint);
            if (index >= arguments.length) {
                throw new IllegalStateException("方法参数下标越界: " + index);
            }
            return arguments[index];
        }

        private static Object argumentNamed(ProceedingJoinPoint joinPoint, String parameterName) {
            Object[] arguments = requireArguments(joinPoint);
            Parameter[] parameters = AdvisorUtil.directParameters(joinPoint);
            if (parameters.length != arguments.length) {
                throw new IllegalStateException("方法参数数量与参数值数量不一致");
            }
            for (int i = 0; i < parameters.length; i++) {
                if (parameterName.equals(parameters[i].getName())) {
                    return arguments[i];
                }
            }
            throw new IllegalStateException("未找到方法参数: " + parameterName);
        }

        private static Object[] requireArguments(ProceedingJoinPoint joinPoint) {
            Object[] arguments = joinPoint.getArgs();
            if (arguments == null) {
                throw new IllegalStateException("方法参数不能为 null");
            }
            return arguments;
        }

        public String getKey() {
            return key;
        }

        public Resolver getResolver() {
            return resolver;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "key='" + key + '\'' +
                    ", resolver=" + resolver +
                    '}';
        }
    }
}
