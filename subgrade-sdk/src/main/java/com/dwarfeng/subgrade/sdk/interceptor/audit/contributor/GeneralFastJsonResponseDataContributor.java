package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 通用 FastJson 响应数据贡献器。
 *
 * <p>
 * 该贡献器将源对象解析为 {@link FastJsonResponseData}，
 * 先记录响应外壳，再按照配置的 {@link Rule} 顺序从响应数据中生成审计字段。外壳固定包含成功标记、返回码和消息。
 * 成功标记仅由元数据返回码是否为 <code>0</code> 决定，与方法是否抛出异常无关，因此控制器捕获异常后返回的失败响应仍会记为失败。
 * 贡献器默认不配置任何数据规则。
 * 规则返回 <code>null</code> 时不生成对应的审计字段，非空结果保持解析器返回的原始类型。
 * 规则生成的审计键统一添加 <code>data.</code> 前缀。
 *
 * <p>
 * 该贡献器不把整个响应数据序列化进审计映射，也不负责类型转换、敏感信息识别、脱敏或截断。
 * 应用应根据审计属性指示器配置规则，或使用专用的 {@link Contributor} 处理敏感信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class GeneralFastJsonResponseDataContributor<T> implements Contributor {

    public static final String KEY_META_CODE = "meta.code";
    public static final String KEY_META_MESSAGE = "meta.message";
    public static final String KEY_PREFIX_DATA = "data.";

    private List<Rule<T>> rules = Collections.emptyList();

    public GeneralFastJsonResponseDataContributor() {
    }

    public GeneralFastJsonResponseDataContributor(List<Rule<T>> rules) {
        setRules(rules);
    }

    @Override
    @Nonnull
    public Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception {
        if (!(source instanceof FastJsonResponseData)) {
            throw new IllegalArgumentException("源对象不是 FastJsonResponseData 类型");
        }
        FastJsonResponseData<?> responseData = (FastJsonResponseData<?>) source;
        FastJsonResponseData.FastJsonMeta meta = responseData.getMeta();
        if (meta == null) {
            throw new IllegalStateException("FastJsonResponseData 的元数据不能为 null");
        }
        if (meta.getMessage() == null) {
            throw new IllegalStateException("FastJsonResponseData 的消息不能为 null");
        }

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(KEY_META_CODE, (long) meta.getCode());
        properties.put(KEY_META_MESSAGE, meta.getMessage());

        @SuppressWarnings("unchecked")
        T data = (T) responseData.getData();
        for (Rule<T> rule : rules) {
            Object value = rule.getResolver().resolve(data);
            if (value == null) {
                continue;
            }
            String key = KEY_PREFIX_DATA + rule.getKey();
            if (properties.containsKey(key)) {
                throw new IllegalStateException("FastJson 响应审计键重复: " + key);
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
            throw new IllegalArgumentException("FastJson 响应规则列表不能为 null");
        }

        List<Rule<T>> copiedRules = new ArrayList<>(rules.size());
        Set<String> keys = new HashSet<>();
        for (Rule<T> rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("FastJson 响应规则列表不能包含 null 元素");
            }
            if (!keys.add(rule.getKey())) {
                throw new IllegalArgumentException("FastJson 响应审计键重复: " + rule.getKey());
            }
            copiedRules.add(rule);
        }
        this.rules = Collections.unmodifiableList(copiedRules);
    }

    @Override
    public String toString() {
        return "GeneralFastJsonResponseDataContributor{" +
                "rules=" + rules +
                '}';
    }

    /**
     * 解析器。
     *
     * <p>
     * 该解析器从响应数据中解析单个审计字段的原始值。
     * 返回值保持原始类型；返回 <code>null</code> 表示不生成对应的审计字段。
     * 响应数据本身为 <code>null</code> 时，贡献器仍会调用解析器。
     */
    @FunctionalInterface
    public interface Resolver<T> {

        /**
         * 解析响应数据字段。
         *
         * @param data 响应数据，可能为 <code>null</code>。
         * @return 解析得到的原始值，或 <code>null</code>。
         * @throws Exception 解析过程中发生的任何异常。
         */
        @Nullable
        Object resolve(@Nullable T data) throws Exception;
    }

    /**
     * 规则。
     *
     * <p>
     * 该规则将一个审计键与一个 {@link Resolver} 绑定。
     * 可通过通用工厂定义任意取值逻辑。
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
         * @param <T>      响应数据的类型。
         * @return 创建的规则。
         */
        public static <T> Rule<T> of(String key, Resolver<T> resolver) {
            return new Rule<>(key, resolver);
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
