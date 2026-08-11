package com.dwarfeng.subgrade.lock.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Lock 模块服务异常帮助类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionHelper {

    /**
     * 添加 Lock 模块的默认异常目标映射。
     *
     * <p>
     * 当前模块没有独占的受检异常类型，因此该方法只负责提供可继续扩展的非空映射。
     *
     * @param map 指定的映射，允许为 null。
     * @return 非空的异常目标映射。
     */
    public static Map<Class<? extends Exception>, Supplier<ServiceException.Code>> putDefaultDestination(
            Map<Class<? extends Exception>, Supplier<ServiceException.Code>> map
    ) {
        return Objects.isNull(map) ? new HashMap<>() : map;
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
