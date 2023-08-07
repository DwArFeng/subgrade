package com.dwarfeng.subgrade.sdk.hibernate.hql;

import com.dwarfeng.subgrade.sdk.hibernate.hql.clause.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public static PredicateClause eq(@Nonnull String expression, @Nonnull Object value) {
        return new EqPredicateClause(expression, value);
    }

    /**
     * 生成等于或者为 null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause eqOrIsNull(@Nonnull String expression, @Nullable Object value) {
        return Objects.isNull(value) ? isNull(expression) : eq(expression, value);
    }

    /**
     * 生成不等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ne(@Nonnull String expression, @Nonnull Object value) {
        return new NePredicateClause(expression, value);
    }

    /**
     * 生成不等于或者为 null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause neOrIsNotNull(@Nonnull String expression, @Nullable Object value) {
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
            @Nonnull String expression,
            @Nonnull String value,
            @Nonnull MatchType matchType
    ) {
        return new LikePredicateClause(expression, value, matchType);
    }

    /**
     * 生成字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause like(@Nonnull String expression, @Nonnull String value) {
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
            @Nonnull String expression,
            @Nonnull String value,
            @Nonnull MatchType matchType) {
        return new CaseSensitiveLikePredicateClause(expression, value, matchType);
    }

    /**
     * 生成大小写敏感的字符串匹配的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ilike(@Nonnull String expression, @Nonnull String value) {
        return ilike(expression, value, MatchType.ANYWHERE);
    }

    /**
     * 生成大于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause gt(@Nonnull String expression, @Nonnull Object value) {
        return new GtPredicateClause(expression, value);
    }

    /**
     * 生成小于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause lt(@Nonnull String expression, @Nonnull Object value) {
        return new LtPredicateClause(expression, value);
    }

    /**
     * 生成大于或等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause ge(@Nonnull String expression, @Nonnull Object value) {
        return new GePredicateClause(expression, value);
    }

    /**
     * 生成小于或等于的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value      属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause le(@Nonnull String expression, @Nonnull Object value) {
        return new LePredicateClause(expression, value);
    }

    /**
     * 生成 Between 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param value1     属性值1。
     * @param value2     属性值2。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause between(@Nonnull String expression, @Nonnull Object value1, @Nonnull Object value2) {
        return new BetweenPredicateClause(expression, value1, value2);
    }

    /**
     * 生成 In 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param values     属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause in(@Nonnull String expression, @Nonnull Collection<?> values) {
        return new InPredicateClause(expression, values);
    }

    /**
     * 生成 In 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @param values     属性值。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause in(@Nonnull String expression, @Nonnull Object... values) {
        return in(expression, Arrays.asList(values));
    }

    /**
     * 生成 Is null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause isNull(@Nonnull String expression) {
        return new IsNullPredicateClause(expression);
    }

    /**
     * 生成 Is not null 的 Where 子句。
     *
     * @param expression 属性表达式。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause isNotNull(@Nonnull String expression) {
        return new IsNotNullPredicateClause(expression);
    }

    /**
     * 生成逻辑与的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause and(@Nonnull Collection<PredicateClause> whereClauses) {
        return new AndPredicateClause(whereClauses);
    }

    /**
     * 生成逻辑与的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause and(@Nonnull PredicateClause... whereClauses) {
        return and(Arrays.asList(whereClauses));
    }

    /**
     * 生成逻辑或的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause or(@Nonnull Collection<PredicateClause> whereClauses) {
        return new OrPredicateClause(whereClauses);
    }

    /**
     * 生成逻辑或的 Where 子句。
     *
     * @param whereClauses Where 子句集合。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause or(@Nonnull PredicateClause... whereClauses) {
        return or(Arrays.asList(whereClauses));
    }

    /**
     * 生成逻辑非的 Where 子句。
     *
     * @param whereClause Where 子句。
     * @return 生成的 Where 子句。
     */
    public static PredicateClause not(@Nonnull PredicateClause whereClause) {
        return new NotPredicateClause(whereClause);
    }

    private WhereHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
