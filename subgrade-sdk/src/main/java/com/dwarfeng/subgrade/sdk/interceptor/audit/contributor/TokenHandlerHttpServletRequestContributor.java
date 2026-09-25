package com.dwarfeng.subgrade.sdk.interceptor.audit.contributor;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.Contributor;
import com.dwarfeng.subgrade.stack.handler.TokenHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Token 处理器 HTTP 请求贡献器。
 *
 * <p>
 * 通过 {@link TokenHandler} 解析并记录用户 ID 和登录 ID。
 * Token 处理器未配置或返回空值时，该贡献器将拒绝生成不完整的审计字段。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
public class TokenHandlerHttpServletRequestContributor implements Contributor {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_LOGIN_ID = "login_id";

    private final TokenHandler tokenHandler;

    public TokenHandlerHttpServletRequestContributor(TokenHandler tokenHandler) {
        if (tokenHandler == null) {
            throw new IllegalArgumentException("TokenHandler 不能为 null");
        }
        this.tokenHandler = tokenHandler;
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

        String userId = tokenHandler.getUserId(request);
        String loginId = tokenHandler.getLoginId(request);
        if (userId == null) {
            throw new IllegalStateException("TokenHandler 返回的用户 ID 不能为 null");
        }
        if (loginId == null) {
            throw new IllegalStateException("TokenHandler 返回的登录 ID 不能为 null");
        }

        properties.put(KEY_USER_ID, userId);
        properties.put(KEY_LOGIN_ID, loginId);
        return properties;
    }

    @Override
    public String toString() {
        return "TokenHandlerHttpServletRequestContributor{" +
                "tokenHandler=" + tokenHandler +
                '}';
    }
}
