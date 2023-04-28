package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.handler.TokenHandler;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.PERMISSION_DENIED;

/**
 * Token 处理器权限增强管理器。
 *
 * <p>
 * 该登录增强需要在入口参数中定义一个 {@link HttpServletRequest} 对象，该对象用于获取 token。<br>
 * 同时，该登录增强限定返回类型为 {@link FastJsonResponseData}，如果权限验证失败，则将权限验证失败信息写入到返回值中。
 *
 * @author DwArFeng
 * @since 1.4.0
 */
public class TokenHandlerPermissionRequiredAopManager implements PermissionRequiredAopManager {

    private TokenHandler tokenHandler;

    @Override
    public StringIdKey getUserKey(ProceedingJoinPoint pjp) {
        // 获取 PJP 的参数。
        Object[] args = pjp.getArgs();

        // 遍历参数，寻找 HttpServletRequest 对象。
        for (Object arg : args) {
            // 如果不是 HttpServletRequest 对象，则跳过。
            if (!(arg instanceof HttpServletRequest)) {
                continue;
            }
            // 如果是 HttpServletRequest 对象，则调用 tokenHandler 获取用户主键。
            try {
                return tokenHandler.getUserKey((HttpServletRequest) arg);
            } catch (Exception e) {
                throw new IllegalArgumentException("TokenHandler 解析用户主键失败，异常信息如下：", e);
            }
        }

        // 如果没有找到 HttpServletRequest 对象，则抛出异常。
        throw new IllegalArgumentException(
                "未能在入口参数中找到 HttpServletRequest 对象，请在入口参数中添加一个 HttpServletRequest 对象"
        );
    }

    @Override
    public Object onMissingPermission(ProceedingJoinPoint pjp, StringIdKey userKey, List<String> missingPermissions) {
        return FastJsonResponseData.of(new ResponseData<>(
                null, new ResponseData.Meta(PERMISSION_DENIED.getCode(), PERMISSION_DENIED.getTip())
        ));
    }

    public TokenHandler getTokenHandler() {
        return tokenHandler;
    }

    public void setTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }
}
