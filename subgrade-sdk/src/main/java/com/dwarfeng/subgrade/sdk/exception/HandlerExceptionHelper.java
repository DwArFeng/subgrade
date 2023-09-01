package com.dwarfeng.subgrade.sdk.exception;

import com.dwarfeng.subgrade.sdk.log.SingleLevelLoggerFactory;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.log.SingleLevelLogger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
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
    public static HandlerException parse(@Nonnull Exception e) {
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
    public static void parseThrow(@Nonnull Exception e) throws HandlerException {
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
            @Nonnull String message, @Nonnull LogLevel logLevel, @Nonnull Exception e
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
            @Nonnull String message, @Nonnull LogLevel logLevel, @Nonnull Exception e
    ) throws HandlerException {
        throw logParse(message, logLevel, e);
    }

    private HandlerExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
