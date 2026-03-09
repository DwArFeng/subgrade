package com.dwarfeng.subgrade.impl.expression;

import com.dwarfeng.subgrade.stack.exception.ExpressionParseException;
import com.dwarfeng.subgrade.stack.expression.ExpressionParser;

/**
 * 表达式解析器的抽象实现。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public abstract class AbstractExpressionParser implements ExpressionParser {

    @Override
    public Object parseExpression(String expressionString, Object context)
            throws ExpressionParseException {
        try {
            return doParseExpression(expressionString, context);
        } catch (ExpressionParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpressionParseException(e, expressionString);
        }
    }

    @Override
    public <T> T parseExpression(String expressionString, Object context, Class<T> clazz)
            throws ExpressionParseException {
        try {
            return doParseExpression(expressionString, context, clazz);
        } catch (ExpressionParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpressionParseException(e, expressionString);
        }
    }

    /**
     * 解析表达式并返回结果。
     *
     * <p>
     * 实现该方法时，只需注重业务逻辑，如果解析过程中发生了任何异常，只需要将原始异常抛出即可。
     *
     * @param expressionString 待解析的表达式字符串。
     * @param context          解析上下文，可为 {@code null}。
     * @return 解析结果。
     * @throws Exception 解析过程中发生的任何异常。
     */
    protected abstract Object doParseExpression(String expressionString, Object context) throws Exception;

    /**
     * 解析表达式并返回结果。
     *
     * <p>
     * 实现该方法时，只需注重业务逻辑，如果解析过程中发生了任何异常，只需要将原始异常抛出即可。
     *
     * <p>
     * 在一般情况下，该方法只需要调用 {@link #doParseExpression(String, Object)} 方法即可。<br>
     * 对于一些特殊的解析器，不同的 <code>class</code> 可能对解析行为有影响时，需要重写该方法。
     *
     * @param expressionString 待解析的表达式字符串。
     * @param context          解析上下文，可为 {@code null}。
     * @param clazz            期望的返回类型。
     * @param <T>              期望的返回类型。
     * @return 解析并转换后的结果。
     * @throws Exception 解析过程中发生的任何异常。
     */
    protected <T> T doParseExpression(String expressionString, Object context, Class<T> clazz) throws Exception {
        return clazz.cast(doParseExpression(expressionString, context));
    }
}
