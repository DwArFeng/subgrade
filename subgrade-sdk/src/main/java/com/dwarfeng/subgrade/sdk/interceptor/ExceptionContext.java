package com.dwarfeng.subgrade.sdk.interceptor;

import java.util.List;
import java.util.Objects;

/**
 * AOP异常上下文。
 *
 * @author DwArFeng
 * @since 0.1.1-beta
 */
public class ExceptionContext {

    private List<Exception> exceptions;

    public ExceptionContext() {
    }

    public ExceptionContext(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public void throwFirstIfExists() throws Exception {
        if (Objects.nonNull(exceptions) && !exceptions.isEmpty()) {
            throw exceptions.get(0);
        }
    }

    @Override
    public String toString() {
        return "ExceptionContext{" +
                "exceptions=" + exceptions +
                '}';
    }
}
