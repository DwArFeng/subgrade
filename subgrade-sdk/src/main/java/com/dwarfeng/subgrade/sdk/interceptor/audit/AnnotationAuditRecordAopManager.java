package com.dwarfeng.subgrade.sdk.interceptor.audit;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 注解式审计记录增强管理器。
 *
 * <p>
 * 该管理器解析方法和参数上的审计贡献注解，按照参数、调用结果、调用信息的顺序执行对应的 {@link Contributor}，
 * 并将生成的属性合并为一份有序映射。Contributor 由注解中的 Bean 名称选择，属性前缀由该管理器统一添加。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class AnnotationAuditRecordAopManager implements AuditRecordAopManager {

    private Map<String, Contributor> contributors = Collections.emptyMap();

    public AnnotationAuditRecordAopManager() {
    }

    public AnnotationAuditRecordAopManager(Map<String, Contributor> contributors) {
        setContributors(contributors);
    }

    @Override
    public Map<String, Object> resolveProperties(AuditRecordContext context) throws Exception {
        Method method = AdvisorUtil.directMethod(context.getPjp());
        Object[] arguments = context.getPjp().getArgs();
        Parameter[] parameters = method.getParameters();
        if (parameters.length != arguments.length) {
            throw new IllegalStateException("审计记录上下文中的参数数量与方法参数数量不一致");
        }

        Map<String, Object> properties = new LinkedHashMap<>();

        // 参数贡献按照参数下标和同一参数上的注解声明顺序执行。
        for (int i = 0; i < parameters.length; i++) {
            AuditParamContribute[] annotations = parameters[i].getAnnotationsByType(AuditParamContribute.class);
            for (AuditParamContribute annotation : annotations) {
                contribute(properties, annotation.contributor(), annotation.prefix(), context, arguments[i]);
            }
        }

        // 正常返回和异常结束对应不同的贡献入口。
        if (context.isSuccess()) {
            for (AuditResultContribute annotation : method.getAnnotationsByType(AuditResultContribute.class)) {
                contribute(properties, annotation.contributor(), annotation.prefix(), context, context.getResult());
            }
        } else {
            for (AuditThrowableContribute annotation : method.getAnnotationsByType(AuditThrowableContribute.class)) {
                contribute(
                        properties, annotation.contributor(), annotation.prefix(), context, context.getThrowable()
                );
            }
        }

        // 调用贡献没有特定的源对象，在两种执行结果下均会执行。
        for (AuditInvocationContribute annotation : method.getAnnotationsByType(AuditInvocationContribute.class)) {
            contribute(properties, annotation.contributor(), annotation.prefix(), context, null);
        }
        return properties;
    }

    // 为了保证代码的可读性，此处代码不做简化。
    @SuppressWarnings({"ConstantValue", "ExtractMethodRecommender"})
    private void contribute(
            Map<String, Object> properties, String contributorName, String prefix,
            AuditRecordContext context, @Nullable Object source
    ) throws Exception {
        if (contributorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Contributor Bean 名称不能为空");
        }
        Contributor contributor = contributors.get(contributorName);
        if (Objects.isNull(contributor)) {
            throw new IllegalStateException("未找到指定的 Contributor Bean: " + contributorName);
        }

        Map<String, Object> contributedProperties = contributor.resolveProperties(context, source);
        if (Objects.isNull(contributedProperties)) {
            throw new IllegalStateException("Contributor 生成了 null 属性映射: " + contributorName);
        }

        // 先完整校验并添加前缀，再写入最终映射，避免单次贡献只合并一部分属性。
        Map<String, Object> resolvedProperties = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : contributedProperties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (Objects.isNull(key)) {
                throw new IllegalStateException("Contributor 生成了 null 属性键: " + contributorName);
            }
            if (Objects.isNull(value)) {
                throw new IllegalStateException("Contributor 生成了 null 属性值: " + contributorName + ", key = " + key);
            }
            resolvedProperties.put(prefix + key, value);
        }
        for (String key : resolvedProperties.keySet()) {
            if (properties.containsKey(key)) {
                throw new IllegalStateException("审计记录属性键重复: " + key);
            }
        }
        properties.putAll(resolvedProperties);
    }

    public Map<String, Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(Map<String, Contributor> contributors) {
        if (Objects.isNull(contributors)) {
            throw new IllegalArgumentException("Contributor Bean 映射不能为 null");
        }
        if (contributors.containsKey(null) || contributors.containsValue(null)) {
            throw new IllegalArgumentException("Contributor Bean 映射不能包含 null 键或值");
        }
        this.contributors = Collections.unmodifiableMap(new LinkedHashMap<>(contributors));
    }

    @Override
    public String toString() {
        return "AnnotationAuditRecordAopManager{" +
                "contributors=" + contributors +
                '}';
    }
}
