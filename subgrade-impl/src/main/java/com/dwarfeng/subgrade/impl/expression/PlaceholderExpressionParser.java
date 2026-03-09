package com.dwarfeng.subgrade.impl.expression;

import com.dwarfeng.subgrade.stack.expression.ExpressionParser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.Objects;

/**
 * 占位符表达式解析器。
 *
 * <p>
 * 采用代理模式，仅解析 {@code ${xxx}} 占位符，解析完成后将实际表达式委托给内部的 {@link ExpressionParser} 进行解析。
 * {@code context} 透传给委托实现，其类型应与委托的 ExpressionParser 所期望的一致。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public class PlaceholderExpressionParser extends AbstractExpressionParser {

    private ConfigurableBeanFactory beanFactory;
    private ExpressionParser delegate;

    public PlaceholderExpressionParser(
            ConfigurableBeanFactory beanFactory,
            ExpressionParser delegate
    ) {
        this.beanFactory = beanFactory;
        this.delegate = delegate;
    }

    @Override
    protected Object doParseExpression(String expressionString, Object context) throws Exception {
        String resolvedExressionString = beanFactory.resolveEmbeddedValue(expressionString);
        if (Objects.isNull(resolvedExressionString)) {
            resolvedExressionString = expressionString;
        }
        return delegate.parseExpression(resolvedExressionString, context);
    }

    public ConfigurableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
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
                "beanFactory=" + beanFactory +
                ", delegate=" + delegate +
                '}';
    }
}
