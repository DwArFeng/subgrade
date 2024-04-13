package com.dwarfeng.subgrade.sdk.log;

import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.log.SingleLevelLogger;
import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

/**
 * 单级日志记录器工厂。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public final class SingleLevelLoggerFactory {

    /**
     * 根据指定的 slf4j logger 和日志等级生成对应的单级日志记录器。
     *
     * @param delegate 指定的 slf4j logger。
     * @param logLevel 指定的日志等级。
     * @return 生成的对应的单级日志记录器。
     */
    public static SingleLevelLogger newInstance(@Nonnull Logger delegate, @Nonnull LogLevel logLevel) {
        switch (logLevel) {
            case DEBUG:
                return new DebugSingleLevelLogger(delegate);
            case INFO:
                return new InfoSingleLevelLogger(delegate);
            case WARN:
                return new WarnSingleLevelLogger(delegate);
            case ERROR:
                return new ErrorSingleLevelLogger(delegate);
            default:
                throw new IllegalArgumentException("未能识别的 logLevel: " + logLevel);
        }
    }

    /**
     * 根据指定的 slf4j logger 和日志等级生成不同等级的单级日志记录器，并且根据日志等级存放在 Map 中。
     *
     * @param delegate 指定的 slf4j logger。
     * @return 根据不同的日志等级存放单级日志记录器的 Map。
     */
    public static Map<LogLevel, SingleLevelLogger> newInstanceMap(@Nonnull Logger delegate) {
        Map<LogLevel, SingleLevelLogger> map = new EnumMap<>(LogLevel.class);
        map.put(LogLevel.DEBUG, new DebugSingleLevelLogger(delegate));
        map.put(LogLevel.INFO, new InfoSingleLevelLogger(delegate));
        map.put(LogLevel.WARN, new WarnSingleLevelLogger(delegate));
        map.put(LogLevel.ERROR, new ErrorSingleLevelLogger(delegate));
        return map;
    }

    /**
     * Debug 级别的单级日志。
     *
     * @author DwArFeng
     * @since 1.2.0
     */
    static class DebugSingleLevelLogger implements SingleLevelLogger {

        private Logger delegate;

        @Override
        public LogLevel logLevel() {
            return LogLevel.DEBUG;
        }

        public DebugSingleLevelLogger(Logger delegate) {
            this.delegate = delegate;
        }

        @Override
        public void log(String msg) {
            delegate.debug(msg);
        }

        @Override
        public void log(String format, Object arg) {
            delegate.debug(format, arg);
        }

        @Override
        public void log(String format, Object arg1, Object arg2) {
            delegate.debug(format, arg1, arg2);
        }

        @Override
        public void log(String format, Object... arguments) {
            delegate.debug(format, arguments);
        }

        @Override
        public void log(String msg, Throwable t) {
            delegate.debug(msg, t);
        }

        @Override
        public void log(Marker marker, String msg) {
            delegate.debug(marker, msg);
        }

        @Override
        public void log(Marker marker, String format, Object arg) {
            delegate.debug(marker, format, arg);
        }

        @Override
        public void log(Marker marker, String format, Object arg1, Object arg2) {
            delegate.debug(marker, format, arg1, arg2);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            delegate.debug(marker, format, arguments);
        }

        @Override
        public void log(Marker marker, String msg, Throwable t) {
            delegate.debug(marker, msg, t);
        }

        public Logger getDelegate() {
            return delegate;
        }

        public void setDelegate(Logger delegate) {
            this.delegate = delegate;
        }
    }

    /**
     * Error 级别的单级日志。
     *
     * @author DwArFeng
     * @since 1.2.0
     */
    static class ErrorSingleLevelLogger implements SingleLevelLogger {

        private Logger delegate;

        @Override
        public LogLevel logLevel() {
            return LogLevel.ERROR;
        }

        public ErrorSingleLevelLogger(Logger delegate) {
            this.delegate = delegate;
        }

        @Override
        public void log(String msg) {
            delegate.error(msg);
        }

        @Override
        public void log(String format, Object arg) {
            delegate.error(format, arg);
        }

        @Override
        public void log(String format, Object arg1, Object arg2) {
            delegate.error(format, arg1, arg2);
        }

        @Override
        public void log(String format, Object... arguments) {
            delegate.error(format, arguments);
        }

        @Override
        public void log(String msg, Throwable t) {
            delegate.error(msg, t);
        }

        @Override
        public void log(Marker marker, String msg) {
            delegate.error(marker, msg);
        }

        @Override
        public void log(Marker marker, String format, Object arg) {
            delegate.error(marker, format, arg);
        }

        @Override
        public void log(Marker marker, String format, Object arg1, Object arg2) {
            delegate.error(marker, format, arg1, arg2);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            delegate.error(marker, format, arguments);
        }

        @Override
        public void log(Marker marker, String msg, Throwable t) {
            delegate.error(marker, msg, t);
        }

        public Logger getDelegate() {
            return delegate;
        }

        public void setDelegate(Logger delegate) {
            this.delegate = delegate;
        }
    }

    /**
     * Info 级别的单级日志。
     *
     * @author DwArFeng
     * @since 1.2.0
     */
    static class InfoSingleLevelLogger implements SingleLevelLogger {

        private Logger delegate;

        @Override
        public LogLevel logLevel() {
            return LogLevel.INFO;
        }

        public InfoSingleLevelLogger(Logger delegate) {
            this.delegate = delegate;
        }

        @Override
        public void log(String msg) {
            delegate.info(msg);
        }

        @Override
        public void log(String format, Object arg) {
            delegate.info(format, arg);
        }

        @Override
        public void log(String format, Object arg1, Object arg2) {
            delegate.info(format, arg1, arg2);
        }

        @Override
        public void log(String format, Object... arguments) {
            delegate.info(format, arguments);
        }

        @Override
        public void log(String msg, Throwable t) {
            delegate.info(msg, t);
        }

        @Override
        public void log(Marker marker, String msg) {
            delegate.info(marker, msg);
        }

        @Override
        public void log(Marker marker, String format, Object arg) {
            delegate.info(marker, format, arg);
        }

        @Override
        public void log(Marker marker, String format, Object arg1, Object arg2) {
            delegate.info(marker, format, arg1, arg2);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            delegate.info(marker, format, arguments);
        }

        @Override
        public void log(Marker marker, String msg, Throwable t) {
            delegate.info(marker, msg, t);
        }

        public Logger getDelegate() {
            return delegate;
        }

        public void setDelegate(Logger delegate) {
            this.delegate = delegate;
        }
    }

    /**
     * Warn 级别的单级日志。
     *
     * @author DwArFeng
     * @since 1.2.0
     */
    static class WarnSingleLevelLogger implements SingleLevelLogger {

        private Logger delegate;

        public WarnSingleLevelLogger(Logger delegate) {
            this.delegate = delegate;
        }

        @Override
        public LogLevel logLevel() {
            return LogLevel.WARN;
        }

        @Override
        public void log(String msg) {
            delegate.warn(msg);
        }

        @Override
        public void log(String format, Object arg) {
            delegate.warn(format, arg);
        }

        @Override
        public void log(String format, Object arg1, Object arg2) {
            delegate.warn(format, arg1, arg2);
        }

        @Override
        public void log(String format, Object... arguments) {
            delegate.warn(format, arguments);
        }

        @Override
        public void log(String msg, Throwable t) {
            delegate.warn(msg, t);
        }

        @Override
        public void log(Marker marker, String msg) {
            delegate.warn(marker, msg);
        }

        @Override
        public void log(Marker marker, String format, Object arg) {
            delegate.warn(marker, format, arg);
        }

        @Override
        public void log(Marker marker, String format, Object arg1, Object arg2) {
            delegate.warn(marker, format, arg1, arg2);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            delegate.warn(marker, format, arguments);
        }

        @Override
        public void log(Marker marker, String msg, Throwable t) {
            delegate.warn(marker, msg, t);
        }

        public Logger getDelegate() {
            return delegate;
        }

        public void setDelegate(Logger delegate) {
            this.delegate = delegate;
        }
    }

    private SingleLevelLoggerFactory() {
        throw new IllegalStateException("禁止实例化");
    }
}
