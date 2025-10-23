package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.LOGIN_FAILED;
import static com.dwarfeng.subgrade.stack.bean.dto.ResponseData.Meta;

/**
 * Http 登录增强管理器。
 *
 * <p>
 * 该登录增强需要在入口参数中定义一个 {@link HttpServletRequest} 对象，该对象用于获取 token。<br>
 * 同时，该登录增强限定返回类型为 {@link FastJsonResponseData}，如果登录失败，则将登录失败信息写入到返回值中。
 *
 * @author DwArFeng
 * @since 1.7.0
 */
public class HttpLoginRequiredAopManager implements LoginRequiredAopManager {

    private String tokenKey;

    @Override
    public String getLoginId(ProceedingJoinPoint pjp) {
        // 获取 PJP 的参数。
        Object[] args = pjp.getArgs();

        // 遍历参数，寻找 HttpServletRequest 对象。
        for (Object arg : args) {
            // 如果不是 HttpServletRequest 对象，则跳过。
            if (!(arg instanceof HttpServletRequest)) {
                continue;
            }
            // 如果是 HttpServletRequest 对象，则获取 header 中的 tokenKey 对应的值。
            String header = ((HttpServletRequest) arg).getHeader(tokenKey);
            if (Objects.isNull(header)) {
                throw new IllegalArgumentException("HttpServletRequest 对象没有名称为 " + tokenKey + " 的 header");
            }
            return header;
        }

        // 如果没有找到 HttpServletRequest 对象，则抛出异常。
        throw new IllegalArgumentException(
                "未能在入口参数中找到 HttpServletRequest 对象，请在入口参数中添加一个 HttpServletRequest 对象"
        );
    }

    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, String loginId) {
        return FastJsonResponseData.of(new ResponseData<>(
                null, new Meta(LOGIN_FAILED.getCode(), LOGIN_FAILED.getTip())
        ));
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
