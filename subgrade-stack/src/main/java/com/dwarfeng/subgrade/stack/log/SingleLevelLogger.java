package com.dwarfeng.subgrade.stack.log;

import org.slf4j.Marker;

/**
 * 单级日志记录器。
 *
 * <p>
 * 不分级别的日志接口，方法与 <code>slf4j</code> 中的日志记录器接口一致。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
public interface SingleLevelLogger {

    /**
     * 获取当前日志记录器的记录等级。
     *
     * @return 当前日志记录器的记录等级。
     */
    LogLevel logLevel();

    void log(String msg);

    void log(String format, Object arg);

    void log(String format, Object arg1, Object arg2);

    void log(String format, Object... arguments);

    void log(String msg, Throwable t);

    void log(Marker marker, String msg);

    void log(Marker marker, String format, Object arg);

    void log(Marker marker, String format, Object arg1, Object arg2);

    void log(Marker marker, String format, Object... arguments);

    void log(Marker marker, String msg, Throwable t);
}
