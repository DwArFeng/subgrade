package com.dwarfeng.subgrade.sdk.interceptor.audit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * 贡献器。
 *
 * <p>
 * 贡献器根据指定的源对象和审计记录上下文生成一次审计记录中的部分属性。
 * 源对象由贡献注解所在的位置决定，可以是入口参数、返回值、异常，也可以为空。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public interface Contributor {

    /**
     * 解析审计属性。
     *
     * @param context 审计记录上下文。
     * @param source  源对象。
     * @return 审计属性组成的映射。
     * @throws Exception 属性解析过程中发生的任何异常。
     */
    @Nonnull
    Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception;
}
