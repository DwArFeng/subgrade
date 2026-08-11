package com.dwarfeng.subgrade.expression.impl.parser;

import com.dwarfeng.subgrade.expression.sdk.parser.AbstractExpressionParser;
import com.dwarfeng.subgrade.expression.stack.parser.ExpressionParser;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * 占位符表达式解析器。
 *
 * <p>
 * 采用代理模式，通过调用方提供的字符串解析器处理 {@code ${xxx}} 等占位符，解析完成后将实际表达式委托给内部的
 * {@link ExpressionParser} 进行解析。{@code context} 透传给委托实现，其类型应与委托解析器所期望的一致。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class PlaceholderExpressionParser extends AbstractExpressionParser {

    private UnaryOperator<String> placeholderResolver;
    private ExpressionParser delegate;

    public PlaceholderExpressionParser(
            UnaryOperator<String> placeholderResolver,
            ExpressionParser delegate
    ) {
        this.placeholderResolver = placeholderResolver;
        this.delegate = delegate;
    }

    @Override
    protected Object doParseExpression(String expressionString, Object context) throws Exception {
        String resolvedExpressionString = placeholderResolver.apply(expressionString);
        if (Objects.isNull(resolvedExpressionString)) {
            resolvedExpressionString = expressionString;
        }
        return delegate.parseExpression(resolvedExpressionString, context);
    }

    public UnaryOperator<String> getPlaceholderResolver() {
        return placeholderResolver;
    }

    public void setPlaceholderResolver(UnaryOperator<String> placeholderResolver) {
        this.placeholderResolver = placeholderResolver;
    }

    public ExpressionParser getDelegate() {
        return delegate;
    }

    public void setDelegate(ExpressionParser delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return "PlaceholderExpressionParser{" +
                "placeholderResolver=" + placeholderResolver +
                ", delegate=" + delegate +
                '}';
    }
}
