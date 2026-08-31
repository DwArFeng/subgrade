package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.util.Map;

/**
 * 属性贡献器。
 *
 * <p>
 * 属性贡献器用于生成一次中的部分属性。
 * 多个贡献器可以由 {@link ContributorAuditRecordAopManager} 组合，从而复用通用的请求、返回值和异常处理逻辑。
 *
 * @author DwArFeng
 * @since 1.9.0
 */
public interface PropertyContributor {

    /**
     * 判断是否支持指定的上下文。
     *
     * @param context 上下文。
     * @return 是否支持指定的上下文。
     */
    boolean supports(AuditRecordContext context);

    /**
     * 获取审计属性。
     *
     * @param context 上下文。
     * @return 审计属性组成的映射。
     * @throws Exception 属性生成过程中发生异常。
     */
    Map<String, Object> getProperties(AuditRecordContext context) throws Exception;
}
