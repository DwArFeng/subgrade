package com.dwarfeng.subgrade.web.sdk.interceptor.http;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.web.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.web.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.web.sdk.exception.ServiceExceptionCodeSuppliers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.validation.BindingResult;

/**
 * BindingCheckAopManager 的默认实现。
 *
 * <p>
 * 当任何 BindingResult 有错误时，返回错误类型为 {@link ServiceExceptionCodeSuppliers#PARAM_VALIDATION_FAILED}
 * 的 bad 类型的 FastJsonResponseData
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class DefaultBindingCheckAopManager implements BindingCheckAopManager {

    @Override
    public Object onHasError(ProceedingJoinPoint pjp, BindingResult bindingResult) {
        return FastJsonResponseData.of(
                ResponseDataUtil.bad(new ServiceException(ServiceExceptionCodeSuppliers.PARAM_VALIDATION_FAILED.get()))
        );
    }
}
