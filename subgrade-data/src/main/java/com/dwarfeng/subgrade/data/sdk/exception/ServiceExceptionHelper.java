package com.dwarfeng.subgrade.data.sdk.exception;

import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.data.stack.exception.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 数据模块服务异常帮助类。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class ServiceExceptionHelper {

    /**
     * 添加数据模块的默认异常目标映射。
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
        map.put(CacheException.class, ServiceExceptionCodeSuppliers.CACHE_FAILED);
        map.put(DaoException.class, ServiceExceptionCodeSuppliers.DAO_FAILED);
        map.put(EntityExistedException.class, ServiceExceptionCodeSuppliers.ENTITY_EXISTED);
        map.put(EntityNotExistException.class, ServiceExceptionCodeSuppliers.ENTITY_NOT_EXIST);
        map.put(DatabaseException.class, ServiceExceptionCodeSuppliers.DATABASE_FAILED);
        return map;
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
