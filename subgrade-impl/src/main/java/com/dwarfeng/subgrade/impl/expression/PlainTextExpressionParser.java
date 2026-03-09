package com.dwarfeng.subgrade.impl.expression;

/**
 * 纯文本表达式解析器。
 *
 * <p>
 * 将表达式字符串原样返回，不进行任何解析。{@code context} 参数被忽略，可为 {@code null}。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class PlainTextExpressionParser extends AbstractExpressionParser {

    @Override
    protected Object doParseExpression(String expressionString, Object context) {
        return expressionString;
    }
}
