package com.dwarfeng.subgrade.stack.exception;

/**
 * 表达式解析异常。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class ExpressionParseException extends Exception {

    private static final long serialVersionUID = 8428843785500904446L;

    private final String expressionString;

    public ExpressionParseException(String expressionString) {
        this.expressionString = expressionString;
    }

    public ExpressionParseException(Throwable cause, String expressionString) {
        super(cause);
        this.expressionString = expressionString;
    }

    @Override
    public String getMessage() {
        return "无法解析表达式: " + expressionString;
    }
}
