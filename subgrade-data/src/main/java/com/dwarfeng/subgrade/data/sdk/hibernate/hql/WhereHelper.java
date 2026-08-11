package com.dwarfeng.subgrade.data.sdk.hibernate.hql;

import com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Where 子句帮助类。
 *
 * @author DwArFeng
 * @since 1.4.2
 */
public final class WhereHelper {

    /**
     * 生成等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause eq(@NotNull String expression, @NotNull Object value) {
        return new EqPredicateClause(expression, value);
    }

    /**
     * 生成等于或者为 null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause eqOrIsNull(@NotNull String expression, @Nullable Object value) {
        return Objects.isNull(value) ? isNull(expression) : eq(expression, value);
    }

    /**
     * 生成不等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ne(@NotNull String expression, @NotNull Object value) {
        return new NePredicateClause(expression, value);
    }

    /**
     * 生成不等于或者为 null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause neOrIsNotNull(@NotNull String expression, @Nullable Object value) {
        return Objects.isNull(value) ? isNotNull(expression) : ne(expression, value);
    }

    /**
     * 生成字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @param matchType  匹配类型。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause like(
            @NotNull String expression,
            @NotNull String value,
            @NotNull MatchType matchType
    ) {
        return new LikePredicateClause(expression, value, matchType);
    }

    /**
     * 生成字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause like(@NotNull String expression, @NotNull String value) {
        return like(expression, value, MatchType.ANYWHERE);
    }

    /**
     * 生成大小写敏感的字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @param matchType  匹配类型。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ilike(
            @NotNull String expression,
            @NotNull String value,
            @NotNull MatchType matchType) {
        return new CaseSensitiveLikePredicateClause(expression, value, matchType);
    }

    /**
     * 生成大小写敏感的字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ilike(@NotNull String expression, @NotNull String value) {
        return ilike(expression, value, MatchType.ANYWHERE);
    }

    /**
     * 生成大于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause gt(@NotNull String expression, @NotNull Object value) {
        return new GtPredicateClause(expression, value);
    }

    /**
     * 生成小于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause lt(@NotNull String expression, @NotNull Object value) {
        return new LtPredicateClause(expression, value);
    }

    /**
     * 生成大于或等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ge(@NotNull String expression, @NotNull Object value) {
        return new GePredicateClause(expression, value);
    }

    /**
     * 生成小于或等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause le(@NotNull String expression, @NotNull Object value) {
        return new LePredicateClause(expression, value);
    }

    /**
     * 生成 Between 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value1     属性值 1。
     * @param value2     属性值 2。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause between(@NotNull String expression, @NotNull Object value1, @NotNull Object value2) {
        return new BetweenPredicateClause(expression, value1, value2);
    }

    /**
     * 生成 In 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param values     属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause in(@NotNull String expression, @NotNull Collection<?> values) {
        return new InPredicateClause(expression, values);
    }

    /**
     * 生成 In 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param values     属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause in(@NotNull String expression, @NotNull Object... values) {
        return in(expression, Arrays.asList(values));
    }

    /**
     * 生成 Is null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause isNull(@NotNull String expression) {
        return new IsNullPredicateClause(expression);
    }

    /**
     * 生成 Is not null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause isNotNull(@NotNull String expression) {
        return new IsNotNullPredicateClause(expression);
    }

    /**
     * 生成逻辑与的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause and(@NotNull Collection<PredicateClause> whereClauses) {
        return new AndPredicateClause(whereClauses);
    }

    /**
     * 生成逻辑与的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause and(@NotNull PredicateClause... whereClauses) {
        return and(Arrays.asList(whereClauses));
    }

    /**
     * 生成逻辑或的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause or(@NotNull Collection<PredicateClause> whereClauses) {
        return new OrPredicateClause(whereClauses);
    }

    /**
     * 生成逻辑或的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause or(@NotNull PredicateClause... whereClauses) {
        return or(Arrays.asList(whereClauses));
    }

    /**
     * 生成逻辑非的 Where 子句。
     *
     * @param whereClause Where 子句。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause not(@NotNull PredicateClause whereClause) {
        return new NotPredicateClause(whereClause);
    }

    private WhereHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
