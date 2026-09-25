package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通用 HTTP 请求贡献器。
 *
 * <p>
 * 该贡献器将源对象解析为 {@link HttpServletRequest}，并按照配置的 {@link Rule} 顺序生成审计字段。
 * 贡献器默认不配置任何规则，可通过规则读取标准请求信息、指定的请求头、请求参数、请求属性或其它派生信息。
 * 所有非空解析结果均转换为字符串，空解析结果不会生成对应的审计字段。
 *
 * <p>
 * 该贡献器不负责敏感信息识别、脱敏、截断或可信代理解析。
 * 应用应根据自身安全要求配置规则，或使用专用的 {@link Contributor} 处理敏感信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class GeneralHttpServletRequestContributor implements Contributor {

    private List<Rule> rules = Collections.emptyList();

    public GeneralHttpServletRequestContributor() {
    }

    public GeneralHttpServletRequestContributor(List<Rule> rules) {
        setRules(rules);
    }

    @Override
    @Nonnull
    public Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception {
        if (!(source instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("源对象不是 HttpServletRequest 类型");
        }
        HttpServletRequest request = (HttpServletRequest) source;
        Map<String, Object> properties = new LinkedHashMap<>();
        for (Rule rule : rules) {
            Object value = rule.getResolver().resolve(request);
            if (value == null) {
                continue;
            }
            String key = rule.getKey();
            if (properties.containsKey(key)) {
                throw new IllegalStateException("HTTP 请求审计键重复: " + key);
            }
            properties.put(key, String.valueOf(value));
        }
        return properties;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("HTTP 请求规则列表不能为 null");
        }

        List<Rule> copiedRules = new ArrayList<>(rules.size());
        Set<String> keys = new HashSet<>();
        for (Rule rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("HTTP 请求规则列表不能包含 null 元素");
            }
            if (!keys.add(rule.getKey())) {
                throw new IllegalArgumentException("HTTP 请求审计键重复: " + rule.getKey());
            }
            copiedRules.add(rule);
        }
        this.rules = Collections.unmodifiableList(copiedRules);
    }

    @Override
    public String toString() {
        return "GeneralHttpServletRequestContributor{" +
                "rules=" + rules +
                '}';
    }

    /**
     * 解析器。
     *
     * <p>
     * 该解析器从指定的 {@link HttpServletRequest} 中解析单个审计字段的原始值。
     * 返回值由贡献器统一转换为字符串；
     * 返回 <code>null</code> 表示不生成对应的审计字段。
     */
    @FunctionalInterface
    public interface Resolver {

        /**
         * 解析 HTTP 请求字段。
         *
         * @param request 指定的 HTTP 请求。
         * @return 解析得到的原始值，或 <code>null</code>。
         * @throws Exception 解析过程中发生的任何异常。
         */
        @Nullable
        Object resolve(@Nonnull HttpServletRequest request) throws Exception;
    }

    /**
     * 规则。
     *
     * <p>
     * 该规则将一个审计键与一个 {@link Resolver} 绑定。
     * 可通过通用工厂定义任意取值逻辑，也可通过专用工厂读取指定的请求头、请求参数或请求属性。
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
         * 创建请求头规则。
         *
         * @param key        审计键。
         * @param headerName 请求头名称。
         * @return 创建的规则。
         */
        public static Rule header(String key, String headerName) {
            requireSourceName(headerName, "请求头名称不能为空");
            return of(key, request -> request.getHeader(headerName));
        }

        /**
         * 创建请求参数规则。
         *
         * @param key           审计键。
         * @param parameterName 请求参数名称。
         * @return 创建的规则。
         */
        public static Rule parameter(String key, String parameterName) {
            requireSourceName(parameterName, "请求参数名称不能为空");
            return of(key, request -> request.getParameter(parameterName));
        }

        /**
         * 创建请求属性规则。
         *
         * @param key           审计键。
         * @param attributeName 请求属性名称。
         * @return 创建的规则。
         */
        public static Rule attribute(String key, String attributeName) {
            requireSourceName(attributeName, "请求属性名称不能为空");
            return of(key, request -> request.getAttribute(attributeName));
        }

        private static void requireSourceName(String sourceName, String message) {
            if (sourceName == null || sourceName.trim().isEmpty()) {
                throw new IllegalArgumentException(message);
            }
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
