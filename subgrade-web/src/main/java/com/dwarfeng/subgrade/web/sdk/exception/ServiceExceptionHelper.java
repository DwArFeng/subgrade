package com.dwarfeng.subgrade.web.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.web.stack.exception.LoginFailedException;
import com.dwarfeng.subgrade.web.stack.exception.PermissionDeniedException;
import com.dwarfeng.subgrade.web.stack.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Web 模块服务异常帮助类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionHelper {

    /**
     * 添加 Web 模块的默认异常目标映射。
     *
     * @param map 指定的映射，允许为 null。
     * @return 添加默认目标后的映射。
     */
    public static Map<Class<? extends Exception>, Supplier<ServiceException.Code>> putDefaultDestination(
            Map<Class<? extends Exception>, Supplier<ServiceException.Code>> map
    ) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }
        map.put(ValidationException.class, ServiceExceptionCodeSuppliers.PARAM_VALIDATION_FAILED);
        map.put(PermissionDeniedException.class, ServiceExceptionCodeSuppliers.PERMISSION_DENIED);
        map.put(LoginFailedException.class, ServiceExceptionCodeSuppliers.LOGIN_FAILED);
        return map;
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
