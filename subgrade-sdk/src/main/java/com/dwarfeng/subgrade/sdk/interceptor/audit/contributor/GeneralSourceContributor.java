package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 通用源对象贡献器。
 *
 * <p>
 * 该贡献器将注解选中的源对象按配置的 {@link Rule} 顺序解析为审计字段。
 * 源对象可以是入口参数、返回值或异常。贡献器默认不配置任何规则。
 * 规则返回 <code>null</code> 时不生成对应的审计字段，非空结果保持解析器返回的原始类型。
 * 源对象为 <code>null</code> 时返回空映射，且不调用规则。
 *
 * <p>
 * {@link Rule#value(String)} 只接受字符串和数字，并原样写入审计映射。
 * 该贡献器不把未配置的源对象内容写入审计映射，也不负责类型转换、敏感信息识别、脱敏或截断。
 * 应用应根据审计需要配置规则，或使用专用的 {@link Contributor} 处理敏感信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class GeneralSourceContributor<T> implements Contributor {

    private List<Rule<T>> rules = Collections.emptyList();

    public GeneralSourceContributor() {
    }

    public GeneralSourceContributor(List<Rule<T>> rules) {
        setRules(rules);
    }

    @Override
    @Nonnull
    public Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception {
        if (source == null) {
            return new LinkedHashMap<>();
        }

        Map<String, Object> properties = new LinkedHashMap<>();
        for (Rule<T> rule : rules) {
            @SuppressWarnings("unchecked")
            T typedSource = (T) source;
            Object value = rule.getResolver().resolve(typedSource);
            if (value == null) {
                continue;
            }
            String key = rule.getKey();
            if (properties.containsKey(key)) {
                throw new IllegalStateException("源对象审计键重复: " + key);
            }
            properties.put(key, value);
        }
        return properties;
    }

    public List<Rule<T>> getRules() {
        return rules;
    }

    public void setRules(List<Rule<T>> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("源对象规则列表不能为 null");
        }

        List<Rule<T>> copiedRules = new ArrayList<>(rules.size());
        Set<String> keys = new HashSet<>();
        for (Rule<T> rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("源对象规则列表不能包含 null 元素");
            }
            if (!keys.add(rule.getKey())) {
                throw new IllegalArgumentException("源对象审计键重复: " + rule.getKey());
            }
            copiedRules.add(rule);
        }
        this.rules = Collections.unmodifiableList(copiedRules);
    }

    @Override
    public String toString() {
        return "GeneralSourceContributor{" +
                "rules=" + rules +
                '}';
    }

    /**
     * 解析器。
     *
     * <p>
     * 该解析器从源对象中解析单个审计字段的原始值。
     * 返回值保持原始类型；返回 <code>null</code> 表示不生成对应的审计字段。
     * 源对象为 <code>null</code> 时，贡献器不调用解析器。
     */
    @FunctionalInterface
    public interface Resolver<T> {

        /**
         * 解析源对象字段。
         *
         * @param source 源对象。
         * @return 解析得到的原始值，或 <code>null</code>。
         * @throws Exception 解析过程中发生的任何异常。
         */
        @Nullable
        Object resolve(@Nonnull T source) throws Exception;
    }

    /**
     * 规则。
     *
     * <p>
     * 该规则将一个审计键与一个 {@link Resolver} 绑定。
     * 可通过通用工厂定义任意取值逻辑，也可通过固定值工厂原样记录字符串或数字。
     */
    public static final class Rule<T> {

        private final String key;
        private final Resolver<T> resolver;

        private Rule(String key, Resolver<T> resolver) {
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
         * @param <T>      源对象的类型。
         * @return 创建的规则。
         */
        public static <T> Rule<T> of(String key, Resolver<T> resolver) {
            return new Rule<>(key, resolver);
        }

        /**
         * 创建固定值规则。
         *
         * <p>
         * 源对象是 {@link String} 或 {@link Number} 时，原样写入审计映射。
         * {@link Integer} 与 {@link Long} 属于数字。其它类型会拒绝解析。
         *
         * @param key 审计键。
         * @param <T> 源对象的类型。
         * @return 创建的规则。
         */
        public static <T> Rule<T> value(String key) {
            return of(key, source -> {
                if (source instanceof String || source instanceof Number) {
                    return source;
                }
                throw new IllegalArgumentException("固定值解析器只接受字符串或数字");
            });
        }

        public String getKey() {
            return key;
        }

        public Resolver<T> getResolver() {
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
