package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

import static com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.LOGIN_FAILED;
import static com.dwarfeng.subgrade.stack.bean.dto.ResponseData.Meta;

/**
 * Http登录增强管理器。
 * <p>该登录增强需要在入口参数中定义一个HttpServletRequest对象，并且返回类型限定为 FastJsonResponseData。
 * HttpServletRequest对象的token用于提供登陆者的ID，如果登录失败，则相关信息会体现在返回的 FastJsonResponseData中。</p>
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class HttpLoginRequiredAopManager implements LoginRequiredAopManager {

    private String tokenKey;

    @Override
    public LongIdKey getLoginId(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                String header = ((HttpServletRequest) arg).getHeader(tokenKey);
                return new LongIdKey(Long.parseLong(header));
            }
        }
        throw new IllegalArgumentException("未能在入口参数中找到HttpServletRequest对象，" +
                "请在入口参数中添加一个HttpServletRequest对象");
    }

    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, LongIdKey loginId) {
        return FastJsonResponseData.of(
                new ResponseData<>(null, new Meta(LOGIN_FAILED.getCode(), LOGIN_FAILED.getTip())));
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
