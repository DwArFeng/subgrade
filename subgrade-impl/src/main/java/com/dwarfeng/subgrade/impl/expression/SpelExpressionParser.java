package com.dwarfeng.subgrade.impl.expression;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;

import java.util.Objects;

/**
 * SpEL 表达式解析器。
 *
 * <p>
 * 基于 Spring SpEL 进行表达式解析。{@code context} 应为 {@link ParserContext} 类型，用于控制模板解析格式
 * （如 <code>#{expr}</code> 的前缀/后缀）；可为 {@code null}，此时使用默认解析行为。
 *
 * <p>
 * 有关 SPEL 的说明，请参阅以下文档：<br>
 * <a href="https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#expressions">
 * https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#expressions
 * </a>
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class SpelExpressionParser extends AbstractExpressionParser {

    private org.springframework.expression.ExpressionParser expressionParser;

    public SpelExpressionParser(org.springframework.expression.ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    protected Object doParseExpression(String expressionString, Object context) {
        Expression expression;
        if (Objects.isNull(context)) {
            expression = expressionParser.parseExpression(expressionString);
        } else {
            expression = expressionParser.parseExpression(expressionString, (ParserContext) context);
        }
        return expression.getValue();
    }

    @Override
    protected <T> T doParseExpression(String expressionString, Object context, Class<T> clazz) {
        Expression expression;
        if (Objects.isNull(context)) {
            expression = expressionParser.parseExpression(expressionString);
        } else {
            expression = expressionParser.parseExpression(expressionString, (ParserContext) context);
        }
        return expression.getValue(clazz);
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public String toString() {
        return "SpelExpressionParser{" +
                "expressionParser=" + expressionParser +
                '}';
    }
}
