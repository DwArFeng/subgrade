package com.dwarfeng.subgrade.basic.sdk.exception;

import com.dwarfeng.subgrade.basic.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.basic.stack.log.SingleLevelLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 处理器异常帮助类。
 *
 * @author DwArFeng
 * @since 1.4.4
 */
public final class HandlerExceptionHelper {

    private static final Map<LogLevel, SingleLevelLogger> LOGGER_MAP =
            SingleLevelLoggerFactory.newInstanceMap(LoggerFactory.getLogger(HandlerExceptionHelper.class));

    /**
     * 将指定的异常转化为处理器异常。
     *
     * @param e 指定的异常。
     * @return 解析后得到的处理器异常。
     */
    public static HandlerException parse(@NotNull Exception e) {
        if (e instanceof HandlerException) {
            return (HandlerException) e;
        }
        return new HandlerException(e);
    }

    /**
     * 将指定的异常转化为处理器异常，并抛出。
     *
     * @param e 指定的异常。
     * @throws HandlerException 解析后抛出的处理器异常。
     */
    public static void parseThrow(@NotNull Exception e) throws HandlerException {
        throw parse(e);
    }

    /**
     * 日志记录指定的异常，并返回该异常转化为处理器异常。
     *
     * @param message  日志的消息。
     * @param logLevel 日志的等级。
     * @param e        指定的异常。
     * @return 解析后得到的处理器异常。
     */
    public static HandlerException logParse(
            @NotNull String message, @NotNull LogLevel logLevel, @NotNull Exception e
    ) {
        LOGGER_MAP.get(logLevel).log(message, e);
        return parse(e);
    }

    /**
     * 日志记录指定的异常，并返回该异常转化为处理器异常，并抛出。
     *
     * @param message  日志的消息。
     * @param logLevel 日志的等级。
     * @param e        指定的异常。
     * @throws HandlerException 解析后抛出的处理器异常。
     */
    public static void logParseThrow(
            @NotNull String message, @NotNull LogLevel logLevel, @NotNull Exception e
    ) throws HandlerException {
        throw logParse(message, logLevel, e);
    }

    private HandlerExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
