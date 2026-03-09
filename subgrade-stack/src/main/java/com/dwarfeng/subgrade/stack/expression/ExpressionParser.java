package com.dwarfeng.subgrade.stack.expression;

import com.dwarfeng.subgrade.stack.exception.ExpressionParseException;

/**
 * 表达式解析器。
 *
 * <p>
 * 用于解析表达式字符串并返回解析结果。{@code context} 参数可为 {@code null}，其具体语义由各实现类定义。
 *
 * @author DwArFeng
 * @since 1.7.3
 */
public interface ExpressionParser {

    /**
     * 解析表达式并返回结果。
     *
     * @param expressionString 待解析的表达式字符串。
     * @param context          解析上下文，可为 {@code null}。
     * @return 解析后的结果。
     * @throws ExpressionParseException 解析过程中发生的异常。
     */
    Object parseExpression(String expressionString, Object context) throws ExpressionParseException;

    /**
     * 解析表达式并转换为指定类型后返回。
     *
     * @param expressionString 待解析的表达式字符串。
     * @param context          解析上下文，可为 {@code null}。
     * @param clazz            期望的返回类型。
     * @param <T>              期望的返回类型。
     * @return 解析并转换后的结果。
     * @throws ExpressionParseException 解析或转换过程中发生的异常。
     */
    <T> T parseExpression(String expressionString, Object context, Class<T> clazz) throws ExpressionParseException;
}
