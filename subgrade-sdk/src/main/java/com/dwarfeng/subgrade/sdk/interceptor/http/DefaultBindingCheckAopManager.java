package com.dwarfeng.subgrade.sdk.interceptor.http;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.validation.BindingResult;

/**
 * BindingCheckAopManager 的默认实现。
 * <p>当任何 BingdingResult 有错误时，返回错误类型为 {@link ServiceExceptionCodes#PARAM_VALIDATION_FAILED}
 * 的bad类型的 FastJsonResponseData</p>
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class DefaultBindingCheckAopManager implements BindingCheckAopManager {

    @Override
    public Object onHasError(ProceedingJoinPoint pjp, BindingResult bindingResult) {
        return FastJsonResponseData.of(
                ResponseDataUtil.bad(new ServiceException(ServiceExceptionCodes.PARAM_VALIDATION_FAILED)));
    }
}
