package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 通用固定值贡献器。
 *
 * <p>
 * 该贡献器按照配置的 {@link Rule} 顺序生成审计字段。
 * 每条规则通过 {@link Supplier} 提供一个与源对象和连接点无关的固定值。贡献器默认不配置任何规则。
 * 供应器返回 <code>null</code> 时不生成对应的审计字段，非空结果保持供应器返回的原始类型。
 * 同一贡献器实例的全部挂载点得到同一组固定值。
 *
 * <p>
 * 由于不读取源对象，该贡献器可以挂在
 * {@link com.dwarfeng.subgrade.sdk.interceptor.audit.AuditInvocationContribute}、
 * {@link com.dwarfeng.subgrade.sdk.interceptor.audit.AuditParamContribute}、
 * {@link com.dwarfeng.subgrade.sdk.interceptor.audit.AuditResultContribute} 和
 * {@link com.dwarfeng.subgrade.sdk.interceptor.audit.AuditThrowableContribute} 上。
 * 调用贡献传入的源对象为 <code>null</code>，其余注解传入各自的源对象，本贡献器对这两种情况采用同一套规则。
 *
 * <p>
 * 该贡献器不负责敏感信息识别、脱敏或截断。
 * 应用应根据审计需要配置规则，或使用专用的 {@link Contributor} 处理敏感信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class GeneralFixedContributor implements Contributor {

    private List<Rule> rules = Collections.emptyList();

    public GeneralFixedContributor() {
    }

    public GeneralFixedContributor(List<Rule> rules) {
        setRules(rules);
    }

    @Override
    @Nonnull
    public Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception {
        Map<String, Object> properties = new LinkedHashMap<>();
        for (Rule rule : rules) {
            Object value = rule.getSupplier().get();
            if (value == null) {
                continue;
            }
            String key = rule.getKey();
            if (properties.containsKey(key)) {
                throw new IllegalStateException("固定值审计键重复: " + key);
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
            throw new IllegalArgumentException("固定值规则列表不能为 null");
        }

        List<Rule> copiedRules = new ArrayList<>(rules.size());
        Set<String> keys = new HashSet<>();
        for (Rule rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("固定值规则列表不能包含 null 元素");
            }
            if (!keys.add(rule.getKey())) {
                throw new IllegalArgumentException("固定值审计键重复: " + rule.getKey());
            }
            copiedRules.add(rule);
        }
        this.rules = Collections.unmodifiableList(copiedRules);
    }

    @Override
    public String toString() {
        return "GeneralFixedContributor{" +
                "rules=" + rules +
                '}';
    }

    /**
     * 供应器。
     *
     * <p>
     * 该供应器提供单个审计字段的固定值，不接收源对象或审计记录上下文。
     * 返回值保持原始类型；返回 <code>null</code> 表示不生成对应的审计字段。
     */
    @FunctionalInterface
    public interface Supplier {

        /**
         * 提供固定值。
         *
         * @return 固定值，或 <code>null</code>。
         * @throws Exception 提供过程中发生的任何异常。
         */
        @Nullable
        Object get() throws Exception;
    }

    /**
     * 规则。
     *
     * <p>
     * 该规则将一个审计键与一个 {@link Supplier} 绑定。
     * 可通过通用工厂定义任意固定值。
     */
    public static final class Rule {

        private final String key;
        private final Supplier supplier;

        private Rule(String key, Supplier supplier) {
            if (key == null || key.trim().isEmpty()) {
                throw new IllegalArgumentException("审计键不能为空");
            }
            if (supplier == null) {
                throw new IllegalArgumentException("供应器不能为 null");
            }
            this.key = key;
            this.supplier = supplier;
        }

        /**
         * 创建通用规则。
         *
         * @param key      审计键。
         * @param supplier 供应器。
         * @return 创建的规则。
         */
        public static Rule of(String key, Supplier supplier) {
            return new Rule(key, supplier);
        }

        public String getKey() {
            return key;
        }

        public Supplier getSupplier() {
            return supplier;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "key='" + key + '\'' +
                    ", supplier=" + supplier +
                    '}';
        }
    }
}
