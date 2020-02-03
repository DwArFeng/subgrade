package com.dwarfeng.subgrade.sdk.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ExceptionContext工具类。
 *
 * @author DwArFeng
 * @since 0.1.1-beta
 */
public class ExceptionContextUtil {

    /**
     * 向指定的 ExceptionContext 中追加指定的异常。
     *
     * @param exceptionContext 指定的 ExceptionContext。
     * @param exception        指定的异常。
     * @return 追加之后的新的 ExceptionContext。
     */
    public static ExceptionContext append(ExceptionContext exceptionContext, Exception exception) {
        exceptionContext = preSet(exceptionContext);
        exceptionContext.getExceptions().add(exception);
        return exceptionContext;
    }

    /**
     * 向指定的 ExceptionContext 中追加指定的异常。
     *
     * @param exceptionContext 指定的 ExceptionContext。
     * @param exceptions       指定的异常组成的列表。
     * @return 追加之后的新的 ExceptionContext。
     */
    public static ExceptionContext append(ExceptionContext exceptionContext, List<Exception> exceptions) {
        exceptionContext = preSet(exceptionContext);
        exceptionContext.getExceptions().addAll(exceptions);
        return exceptionContext;
    }

    private static ExceptionContext preSet(ExceptionContext exceptionContext) {
        if (Objects.isNull(exceptionContext)) {
            return new ExceptionContext(new ArrayList<>());
        } else if (Objects.isNull(exceptionContext.getExceptions())) {
            exceptionContext.setExceptions(new ArrayList<>());
            return exceptionContext;
        } else {
            return exceptionContext;
        }
    }


}
