package com.dwarfeng.subgrade.basic.sdk.exception;

import com.dwarfeng.dutil.basic.stack.lifecycle.ProcessException;
import com.dwarfeng.subgrade.basic.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.basic.stack.exception.*;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.basic.stack.log.SingleLevelLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 服务异常帮助类。
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

    /**
     * 向指定的映射中添加 subgrade 默认的目标映射。
     *
     * <p>
     * 该方法可以在配置类中快速的搭建目标映射。
     *
     * @param map 指定的映射，允许为 null。
     * @return 添加了默认目标的映射。
     */
    public static Map<Class<? extends Exception>, Supplier<ServiceException.Code>> putDefaultDestination(
            Map<Class<? extends Exception>, Supplier<ServiceException.Code>> map) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }

        map.put(IOException.class, ServiceExceptionCodeSuppliers.IO_EXCEPTION);
        map.put(ProcessException.class, ServiceExceptionCodeSuppliers.PROCESS_FAILED);
        map.put(HandlerException.class, ServiceExceptionCodeSuppliers.HANDLER_FAILED);
        map.put(GenerateException.class, ServiceExceptionCodeSuppliers.GENERATE_FAILED);
        map.put(PagingException.class, ServiceExceptionCodeSuppliers.PAGING_FAILED);

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
     * @deprecated 该方法由于命名不规范，已经被 {@link #logParse(String, LogLevel, Exception, ServiceExceptionMapper)} 取代。
     */
    @Deprecated
    public static ServiceException logAndThrow(
            @NotNull String message, @NotNull LogLevel logLevel, @NotNull ServiceExceptionMapper mapper,
            @NotNull Exception e
    ) {
        LOGGER_MAP.get(logLevel).log(message, e);
        return mapper.map(e);
    }

    /**
     * 映射指定的异常，并抛出映射后的新异常。
     *
     * @param mapper 指定的异常映射器。
     * @param e      指定的异常。
     * @return 转化后抛出的服务异常。
     * @deprecated 该方法由于命名不规范，已经被 {@link #parse(Exception, ServiceExceptionMapper)} 取代。
     */
    @Deprecated
    public static ServiceException mapAndThrow(
            @NotNull ServiceExceptionMapper mapper, @NotNull Exception e
    ) {
        return mapper.map(e);
    }

    /**
     * 将指定的异常解析为服务异常。
     *
     * @param mapper 参与解析的服务异常映射器。
     * @param e      指定的异常。
     * @return 解析后得到的服务异常。
     * @since 1.4.4
     */
    public static ServiceException parse(@NotNull Exception e, @NotNull ServiceExceptionMapper mapper) {
        return mapper.map(e);
    }

    /**
     * 将指定的异常解析为服务异常，并抛出。
     *
     * @param mapper 参与解析的服务异常映射器。
     * @param e      指定的异常。
     * @throws ServiceException 解析后抛出的服务异常。
     * @since 1.4.4
     */
    public static void parseThrow(@NotNull Exception e, @NotNull ServiceExceptionMapper mapper)
            throws ServiceException {
        throw parse(e, mapper);
    }

    /**
     * 日志记录指定的异常，并将该异常解析为服务异常。
     *
     * @param message  日志的消息。
     * @param logLevel 日志的等级。
     * @param e        指定的异常。
     * @param mapper   参与解析的服务异常映射器。
     * @return 解析后得到的服务异常。
     * @since 1.4.4
     */
    public static ServiceException logParse(
            @NotNull String message, @NotNull LogLevel logLevel,
            @NotNull Exception e, @NotNull ServiceExceptionMapper mapper
    ) {
        LOGGER_MAP.get(logLevel).log(message, e);
        return parse(e, mapper);
    }

    /**
     * 日志记录指定的异常，并将该异常解析为服务异常，并抛出。
     *
     * @param message  日志的消息。
     * @param logLevel 日志的等级。
     * @param e        指定的异常。
     * @param mapper   参与解析的服务异常映射器。
     * @throws ServiceException 解析后抛出的服务异常。
     * @since 1.4.4
     */
    public static void logParseThrow(
            @NotNull String message, @NotNull LogLevel logLevel,
            @NotNull Exception e, @NotNull ServiceExceptionMapper mapper
    ) throws ServiceException {
        throw logParse(message, logLevel, e, mapper);
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
