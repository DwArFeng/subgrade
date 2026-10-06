package com.dwarfeng.subgrade.aop.impl.interceptor.audit;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordAopManager;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;

import java.util.*;

/**
 * 贡献器式审计记录增强管理器。
 *
 * <p>
 * 该管理器组合多个 {@link PropertyContributor}，并将所有适用于当前调用的贡献器生成的属性合并为一份审计属性映射。
 * 贡献器按照配置顺序执行，属性键冲突会被视为配置错误。
 *
 * @author DwArFeng
 * @since 2.1.1
 */
public class ContributorAuditRecordAopManager implements AuditRecordAopManager {

    public static final String BEAN_NAME = "contributorAuditRecordAopManager";

    private List<PropertyContributor> contributors = Collections.emptyList();

    public ContributorAuditRecordAopManager() {
    }

    public ContributorAuditRecordAopManager(List<? extends PropertyContributor> contributors) {
        setContributors(contributors);
    }

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) throws Exception {
        Map<String, Object> properties = new LinkedHashMap<>();
        for (PropertyContributor contributor : contributors) {
            if (!contributor.supports(context)) {
                continue;
            }
            Map<String, Object> contributedProperties = contributor.getProperties(context);
            if (Objects.isNull(contributedProperties)) {
                continue;
            }
            for (Map.Entry<String, Object> entry : contributedProperties.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (Objects.isNull(key)) {
                    throw new IllegalStateException("属性贡献器生成了 null 属性键: " + contributor);
                }
                if (Objects.isNull(value)) {
                    continue;
                }
                if (properties.containsKey(key)) {
                    throw new IllegalStateException("审计记录属性键重复: " + key);
                }
                properties.put(key, value);
            }
        }
        return properties;
    }

    public List<PropertyContributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<? extends PropertyContributor> contributors) {
        this.contributors = List.copyOf(contributors);
    }

    @Override
    public String toString() {
        return "ContributorAuditRecordAopManager{" +
                "contributors=" + contributors +
                '}';
    }
}
