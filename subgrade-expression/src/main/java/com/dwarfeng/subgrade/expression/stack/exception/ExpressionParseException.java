package com.dwarfeng.subgrade.expression.stack.exception;

import com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessageKey;
import com.dwarfeng.subgrade.expression.internal.i18n.ExpressionMessages;

import java.io.Serial;

/**
 * 表达式解析异常。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class ExpressionParseException extends Exception {

    @Serial
    private static final long serialVersionUID = -5690481730983184717L;

    private final String expressionString;

    public ExpressionParseException(String expressionString) {
        super(ExpressionMessages.message(ExpressionMessageKey.PARSE_FAILED_DETAIL, expressionString));
        this.expressionString = expressionString;
    }

    public ExpressionParseException(Throwable cause, String expressionString) {
        super(ExpressionMessages.message(ExpressionMessageKey.PARSE_FAILED_DETAIL, expressionString), cause);
        this.expressionString = expressionString;
    }

    @Override
    public String getMessage() {
        return ExpressionMessages.message(ExpressionMessageKey.PARSE_FAILED_DETAIL, expressionString);
    }
}
