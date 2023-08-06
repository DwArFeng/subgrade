package com.dwarfeng.subgrade.sdk.exception;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.subgrade.sdk.interceptor.login.LoginFailedException;
import com.dwarfeng.subgrade.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.stack.exception.*;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.log.SingleLevelLogger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常的帮助类。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ServiceExceptionHelper {

    /**
     * @since 1.2.0
     */
    private static final Map<LogLevel, SingleLevelLogger> LOGGER_MAP =
            SingleLevelLoggerFactory.newInstanceMap(LoggerFactory.getLogger(ServiceExceptionHelper.class));

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止外部实例化");
    }

    /**
     * 向指定的映射中添加subgrade默认的目标映射。
     * <p>该方法可以在配置类中快速的搭建目标映射。</p>
     *
     * @param map 指定的映射，允许为null。
     * @return 添加了默认目标的映射。
     */
    public static Map<Class<? extends Exception>, ServiceException.Code> putDefaultDestination(
            Map<Class<? extends Exception>, ServiceException.Code> map) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }

        map.put(KeyFetchException.class, ServiceExceptionCodes.KEY_FETCH_FAILED);
        map.put(CacheException.class, ServiceExceptionCodes.CACHE_FAILED);
        map.put(DaoException.class, ServiceExceptionCodes.DAO_FAILED);
        map.put(EntityNotExistException.class, ServiceExceptionCodes.ENTITY_NOT_EXIST);
        map.put(EntityExistedException.class, ServiceExceptionCodes.ENTITY_EXISTED);
        map.put(ValidationException.class, ServiceExceptionCodes.PARAM_VALIDATION_FAILED);
        map.put(IOException.class, ServiceExceptionCodes.IO_EXCEPTION);
        map.put(ProcessException.class, ServiceExceptionCodes.PROCESS_FAILED);
        map.put(HandlerException.class, ServiceExceptionCodes.HANDLER_FAILED);
        map.put(PermissionDeniedException.class, ServiceExceptionCodes.PERMISSION_DENIED);
        map.put(LoginFailedException.class, ServiceExceptionCodes.LOGIN_FAILED);

        return map;
    }

    /**
     * 记录指定的异常，并返回该异常转化为服务异常。
     *
     * @param message  指定异常的记录文本。
     * @param logLevel 日志的等级。
     * @param mapper   指定的异常映射器。
     * @param e        指定的异常。
     * @return 转化后抛出的服务异常。
     */
    public static ServiceException logAndThrow(
            @Nonnull String message, @Nonnull LogLevel logLevel, @Nonnull ServiceExceptionMapper mapper,
            @Nonnull Exception e) {
        LOGGER_MAP.get(logLevel).log(message, e);
        return mapper.map(e);
    }

    /**
     * 映射指定的异常，并抛出映射后的新异常。
     *
     * @param mapper 指定的异常映射器。
     * @param e      指定的异常。
     * @return 转化后抛出的服务异常。
     */
    public static ServiceException mapAndThrow(
            @Nonnull ServiceExceptionMapper mapper, @Nonnull Exception e) {
        return mapper.map(e);
    }
}
