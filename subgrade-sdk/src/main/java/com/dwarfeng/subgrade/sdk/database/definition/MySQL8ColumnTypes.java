package com.dwarfeng.subgrade.sdk.database.definition;

import javax.annotation.Nonnull;

/**
 * 列类型工具类。
 *
 * <p>
 * 该工具类用于生成 MySQL8 支持的数据类型对应的字符串。
 *
 * <p>
 * 有关 MySQL8 支持的数据类型，请参考:
 * <a href="https://dev.mysql.com/doc/refman/8.0/en/data-types.html">
 * https://dev.mysql.com/doc/refman/8.0/en/data-types.html
 * </a>
 *
 * @author DwArFeng
 * @since 1.4.7
 */
public final class MySQL8ColumnTypes {

    /**
     * 生成 TINYINT 数据类型对应的列类型字符串。
     *
     * <p>
     * TINYINT 的格式为 <code>TINYINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return TINYINT 数据类型对应的列类型字符串。
     * @see #tinyint(int)
     */
    public static String tinyint() {
        return "TINYINT";
    }

    /**
     * 生成 TINYINT 数据类型对应的列类型字符串。
     *
     * <p>
     * TINYINT 的格式为 <code>TINYINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return TINYINT 数据类型对应的列类型字符串。
     */
    public static String tinyint(int m) {
        return String.format("TINYINT(%d)", m);
    }

    /**
     * 生成 SMALLINT 数据类型对应的列类型字符串。
     *
     * <p>
     * SMALLINT 的格式为 <code>SMALLINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return SMALLINT 数据类型对应的列类型字符串。
     * @see #smallint(int)
     */
    public static String smallint() {
        return "SMALLINT";
    }

    /**
     * 生成 SMALLINT 数据类型对应的列类型字符串。
     *
     * <p>
     * SMALLINT 的格式为 <code>SMALLINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return SMALLINT 数据类型对应的列类型字符串。
     */
    public static String smallint(int m) {
        return String.format("SMALLINT(%d)", m);
    }

    /**
     * 生成 MEDIUMINT 数据类型对应的列类型字符串。
     *
     * <p>
     * MEDIUMINT 的格式为 <code>MEDIUMINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return MEDIUMINT 数据类型对应的列类型字符串。
     * @see #mediumint(int)
     */
    public static String mediumint() {
        return "MEDIUMINT";
    }

    /**
     * 生成 MEDIUMINT 数据类型对应的列类型字符串。
     *
     * <p>
     * MEDIUMINT 的格式为 <code>MEDIUMINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return MEDIUMINT 数据类型对应的列类型字符串。
     */
    public static String mediumint(int m) {
        return String.format("MEDIUMINT(%d)", m);
    }

    /**
     * 生成 INT 数据类型对应的列类型字符串。
     *
     * <p>
     * INT 的格式为 <code>INT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return INT 数据类型对应的列类型字符串。
     * @see #sqlInt(int)
     */
    public static String sqlInt() {
        return "INT";
    }

    /**
     * 生成 INT 数据类型对应的列类型字符串。
     *
     * <p>
     * INT 的格式为 <code>INT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return INT 数据类型对应的列类型字符串。
     */
    public static String sqlInt(int m) {
        return String.format("INT(%d)", m);
    }

    /**
     * 生成 INTEGER 数据类型对应的列类型字符串。
     *
     * <p>
     * INTEGER 是 INT 的同义词。
     *
     * <p>
     * INTEGER 的格式为 <code>INTEGER[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return INTEGER 数据类型对应的列类型字符串。
     * @see #sqlInteger(int)
     */
    public static String sqlInteger() {
        return "INTEGER";
    }

    /**
     * 生成 INTEGER 数据类型对应的列类型字符串。
     *
     * <p>
     * INTEGER 是 INT 的同义词。
     *
     * <p>
     * INTEGER 的格式为 <code>INTEGER[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return INTEGER 数据类型对应的列类型字符串。
     */
    public static String sqlInteger(int m) {
        return String.format("INTEGER(%d)", m);
    }

    /**
     * 生成 BIGINT 数据类型对应的列类型字符串。
     *
     * <p>
     * BIGINT 的格式为 <code>BIGINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return BIGINT 数据类型对应的列类型字符串。
     * @see #bigint(int)
     */
    public static String bigint() {
        return "BIGINT";
    }

    /**
     * 生成 BIGINT 数据类型对应的列类型字符串。
     *
     * <p>
     * BIGINT 的格式为 <code>BIGINT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return BIGINT 数据类型对应的列类型字符串。
     */
    public static String bigint(int m) {
        return String.format("BIGINT(%d)", m);
    }

    /**
     * 生成 FLOAT 数据类型对应的列类型字符串。
     *
     * <p>
     * FLOAT 的格式为 <code>FLOAT[(P|M,D)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return FLOAT 数据类型对应的列类型字符串。
     * @see #sqlFloat(int)
     * @see #sqlFloat(int, int)
     */
    public static String sqlFloat() {
        return "FLOAT";
    }

    /**
     * 生成 FLOAT 数据类型对应的列类型字符串。
     *
     * <p>
     * FLOAT 的格式为 <code>FLOAT[(P|M,D)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param p 参数 P。
     * @return FLOAT 数据类型对应的列类型字符串。
     */
    public static String sqlFloat(int p) {
        return String.format("FLOAT(%d)", p);
    }

    /**
     * 生成 FLOAT 数据类型对应的列类型字符串。
     *
     * <p>
     * FLOAT 的格式为 <code>FLOAT[(P|M,D)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return FLOAT 数据类型对应的列类型字符串。
     */
    public static String sqlFloat(int m, int d) {
        return String.format("FLOAT(%d,%d)", m, d);
    }

    /**
     * 生成 DOUBLE 数据类型对应的列类型字符串。
     *
     * <p>
     * DOUBLE 的格式为 <code>DOUBLE[(M,D)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return DOUBLE 数据类型对应的列类型字符串。
     * @see #sqlDouble(int, int)
     */
    public static String sqlDouble() {
        return "DOUBLE";
    }

    /**
     * 生成 DOUBLE 数据类型对应的列类型字符串。
     *
     * <p>
     * DOUBLE 的格式为 <code>DOUBLE[(M,D)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return DOUBLE 数据类型对应的列类型字符串。
     */
    public static String sqlDouble(int m, int d) {
        return String.format("DOUBLE(%d,%d)", m, d);
    }

    /**
     * 生成 DECIMAL 数据类型对应的列类型字符串。
     *
     * <p>
     * DECIMAL 的格式为 <code>DECIMAL[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return DECIMAL 数据类型对应的列类型字符串。
     * @see #decimal(int)
     * @see #decimal(int, int)
     */
    public static String decimal() {
        return "DECIMAL";
    }

    /**
     * 生成 DECIMAL 数据类型对应的列类型字符串。
     *
     * <p>
     * DECIMAL 的格式为 <code>DECIMAL[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return DECIMAL 数据类型对应的列类型字符串。
     */
    public static String decimal(int m) {
        return String.format("DECIMAL(%d)", m);
    }

    /**
     * 生成 DECIMAL 数据类型对应的列类型字符串。
     *
     * <p>
     * DECIMAL 的格式为 <code>DECIMAL[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return DECIMAL 数据类型对应的列类型字符串。
     */
    public static String decimal(int m, int d) {
        return String.format("DECIMAL(%d,%d)", m, d);
    }

    /**
     * 生成 DEC 数据类型对应的列类型字符串。
     *
     * <p>
     * DEC 是 DECIMAL 的同义词。
     *
     * <p>
     * DEC 的格式为 <code>DEC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return DEC 数据类型对应的列类型字符串。
     * @see #dec(int)
     * @see #dec(int, int)
     */
    public static String dec() {
        return "DEC";
    }

    /**
     * 生成 DEC 数据类型对应的列类型字符串。
     *
     * <p>
     * DEC 是 DECIMAL 的同义词。
     *
     * <p>
     * DEC 的格式为 <code>DEC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return DEC 数据类型对应的列类型字符串。
     */
    public static String dec(int m) {
        return String.format("DEC(%d)", m);
    }

    /**
     * 生成 DEC 数据类型对应的列类型字符串。
     *
     * <p>
     * DEC 是 DECIMAL 的同义词。
     *
     * <p>
     * DEC 的格式为 <code>DEC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return DEC 数据类型对应的列类型字符串。
     */
    public static String dec(int m, int d) {
        return String.format("DEC(%d,%d)", m, d);
    }

    /**
     * 生成 NUMERIC 数据类型对应的列类型字符串。
     *
     * <p>
     * NUMERIC 是 DECIMAL 的同义词。
     *
     * <p>
     * NUMERIC 的格式为 <code>NUMERIC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return NUMERIC 数据类型对应的列类型字符串。
     * @see #numeric(int)
     * @see #numeric(int, int)
     */
    public static String numeric() {
        return "NUMERIC";
    }

    /**
     * 生成 NUMERIC 数据类型对应的列类型字符串。
     *
     * <p>
     * NUMERIC 是 DECIMAL 的同义词。
     *
     * <p>
     * NUMERIC 的格式为 <code>NUMERIC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return NUMERIC 数据类型对应的列类型字符串。
     */
    public static String numeric(int m) {
        return String.format("NUMERIC(%d)", m);
    }

    /**
     * 生成 NUMERIC 数据类型对应的列类型字符串。
     *
     * <p>
     * NUMERIC 是 DECIMAL 的同义词。
     *
     * <p>
     * NUMERIC 的格式为 <code>NUMERIC[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return NUMERIC 数据类型对应的列类型字符串。
     */
    public static String numeric(int m, int d) {
        return String.format("NUMERIC(%d,%d)", m, d);
    }

    /**
     * 生成 FIXED 数据类型对应的列类型字符串。
     *
     * <p>
     * FIXED 是 DECIMAL 的同义词，它可以用来兼容其它数据库系统。
     *
     * <p>
     * FIXED 的格式为 <code>FIXED[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @return FIXED 数据类型对应的列类型字符串。
     * @see #fixed(int)
     * @see #fixed(int, int)
     */
    public static String fixed() {
        return "FIXED";
    }

    /**
     * 生成 FIXED 数据类型对应的列类型字符串。
     *
     * <p>
     * FIXED 是 DECIMAL 的同义词，它可以用来兼容其它数据库系统。
     *
     * <p>
     * FIXED 的格式为 <code>FIXED[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return FIXED 数据类型对应的列类型字符串。
     */
    public static String fixed(int m) {
        return String.format("FIXED(%d)", m);
    }

    /**
     * 生成 FIXED 数据类型对应的列类型字符串。
     *
     * <p>
     * FIXED 是 DECIMAL 的同义词，它可以用来兼容其它数据库系统。
     *
     * <p>
     * FIXED 的格式为 <code>FIXED[(M[,D])]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/numeric-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @param d 参数 D。
     * @return FIXED 数据类型对应的列类型字符串。
     */
    public static String fixed(int m, int d) {
        return String.format("FIXED(%d,%d)", m, d);
    }

    /**
     * 生成 DATE 数据类型对应的列类型字符串。
     *
     * <p>
     * DATE 的格式为 <code>DATE</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @return DATE 数据类型对应的列类型字符串。
     */
    public static String date() {
        return "DATE";
    }

    /**
     * 生成 DATETIME 数据类型对应的列类型字符串。
     *
     * <p>
     * DATETIME 的格式为 <code>DATETIME[(fsp)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @return DATETIME 数据类型对应的列类型字符串。
     * @see #datetime(String)
     */
    public static String datetime() {
        return "DATETIME";
    }

    /**
     * 生成 DATETIME 数据类型对应的列类型字符串。
     *
     * <p>
     * DATETIME 的格式为 <code>DATETIME[(fsp)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @param fsp 参数 fsp。
     * @return DATETIME 数据类型对应的列类型字符串。
     * @see #datetime(String)
     */
    public static String datetime(String fsp) {
        return String.format("DATETIME(%s)", fsp);
    }

    /**
     * 生成 TIMESTAMP 数据类型对应的列类型字符串。
     *
     * <p>
     * TIMESTAMP 的格式为 <code>TIMESTAMP[(fsp)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @return TIMESTAMP 数据类型对应的列类型字符串。
     * @see #timestamp(String)
     */
    public static String timestamp() {
        return "TIMESTAMP";
    }

    /**
     * 生成 TIMESTAMP 数据类型对应的列类型字符串。
     *
     * <p>
     * TIMESTAMP 的格式为 <code>TIMESTAMP[(fsp)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @param fsp 参数 fsp。
     * @return TIMESTAMP 数据类型对应的列类型字符串。
     * @see #timestamp(String)
     */
    public static String timestamp(String fsp) {
        return String.format("TIMESTAMP(%s)", fsp);
    }

    /**
     * 生成 YEAR 数据类型对应的列类型字符串。
     *
     * <p>
     * YEAR 的格式为 <code>YEAR[(4)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @return YEAR 数据类型对应的列类型字符串。
     * @see #year(YearDigit)
     */
    public static String year() {
        return "YEAR";
    }

    /**
     * 生成 YEAR 数据类型对应的列类型字符串。
     *
     * <p>
     * YEAR 的格式为 <code>YEAR[(4)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/date-and-time-type-syntax.html
     * </a>
     *
     * @return YEAR 数据类型对应的列类型字符串。
     */
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static String year(@Nonnull YearDigit yearDigit) {
        switch (yearDigit) {
            case FOUR:
                return "YEAR(4)";
            default:
                return "YEAR";
        }
    }

    /**
     * YEAR 类型的位数。
     *
     * @author DwArFeng
     * @since 1.4.7
     */
    public enum YearDigit {FOUR}

    /**
     * 生成 CHAR 数据类型对应的列类型字符串。
     *
     * <p>
     * CHAR 的格式为 <code>CHAR[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return CHAR 数据类型对应的列类型字符串。
     * @see #sqlChar(int)
     */
    public static String sqlChar() {
        return "CHAR";
    }

    /**
     * 生成 CHAR 数据类型对应的列类型字符串。
     *
     * <p>
     * CHAR 的格式为 <code>CHAR[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return CHAR 数据类型对应的列类型字符串。
     */
    public static String sqlChar(int m) {
        return String.format("CHAR(%d)", m);
    }

    /**
     * 生成 VARCHAR 数据类型对应的列类型字符串。
     *
     * <p>
     * VARCHAR 的格式为 <code>VARCHAR[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return VARCHAR 数据类型对应的列类型字符串。
     * @see #varchar(int)
     */
    public static String varchar() {
        return "VARCHAR";
    }

    /**
     * 生成 VARCHAR 数据类型对应的列类型字符串。
     *
     * <p>
     * VARCHAR 的格式为 <code>VARCHAR[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return VARCHAR 数据类型对应的列类型字符串。
     */
    public static String varchar(int m) {
        return String.format("VARCHAR(%d)", m);
    }

    /**
     * 生成 TINYBLOB 数据类型对应的列类型字符串。
     *
     * <p>
     * TINYBLOB 的格式为 <code>TINYBLOB</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return TINYBLOB 数据类型对应的列类型字符串。
     */
    public static String tinyblob() {
        return "TINYBLOB";
    }

    /**
     * 生成 TINYTEXT 数据类型对应的列类型字符串。
     *
     * <p>
     * TINYTEXT 的格式为 <code>TINYTEXT</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return TINYTEXT 数据类型对应的列类型字符串。
     */
    public static String tinytext() {
        return "TINYTEXT";
    }

    /**
     * 生成 BLOB 数据类型对应的列类型字符串。
     *
     * <p>
     * BLOB 的格式为 <code>BLOB[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return BLOB 数据类型对应的列类型字符串。
     * @see #blob(int)
     */
    public static String blob() {
        return "BLOB";
    }

    /**
     * 生成 BLOB 数据类型对应的列类型字符串。
     *
     * <p>
     * BLOB 的格式为 <code>BLOB[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return BLOB 数据类型对应的列类型字符串。
     */
    public static String blob(int m) {
        return String.format("BLOB(%d)", m);
    }

    /**
     * 生成 TEXT 数据类型对应的列类型字符串。
     *
     * <p>
     * TEXT 的格式为 <code>TEXT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return TEXT 数据类型对应的列类型字符串。
     * @see #text(int)
     */
    public static String text() {
        return "TEXT";
    }

    /**
     * 生成 TEXT 数据类型对应的列类型字符串。
     *
     * <p>
     * TEXT 的格式为 <code>TEXT[(M)]</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param m 参数 M。
     * @return TEXT 数据类型对应的列类型字符串。
     */
    public static String text(int m) {
        return String.format("TEXT(%d)", m);
    }

    /**
     * 生成 MEDIUMBLOB 数据类型对应的列类型字符串。
     *
     * <p>
     * MEDIUMBLOB 的格式为 <code>MEDIUMBLOB</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return MEDIUMBLOB 数据类型对应的列类型字符串。
     */
    public static String mediumblob() {
        return "MEDIUMBLOB";
    }

    /**
     * 生成 MEDIUMTEXT 数据类型对应的列类型字符串。
     *
     * <p>
     * MEDIUMTEXT 的格式为 <code>MEDIUMTEXT</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return MEDIUMTEXT 数据类型对应的列类型字符串。
     */
    public static String mediumtext() {
        return "MEDIUMTEXT";
    }

    /**
     * 生成 LONGBLOB 数据类型对应的列类型字符串。
     *
     * <p>
     * LONGBLOB 的格式为 <code>LONGBLOB</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return LONGBLOB 数据类型对应的列类型字符串。
     */
    public static String longblob() {
        return "LONGBLOB";
    }

    /**
     * 生成 LONGTEXT 数据类型对应的列类型字符串。
     *
     * <p>
     * LONGTEXT 的格式为 <code>LONGTEXT</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @return LONGTEXT 数据类型对应的列类型字符串。
     */
    public static String longtext() {
        return "LONGTEXT";
    }

    /**
     * 生成 ENUM 数据类型对应的列类型字符串。
     *
     * <p>
     * ENUM 的格式为 <code>ENUM('value1','value2',...)</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param values 参数 values。
     * @return ENUM 数据类型对应的列类型字符串。
     */
    @SuppressWarnings({"DuplicatedCode", "JavaExistingMethodCanBeUsed"})
    public static String sqlEnum(String... values) {
        StringBuilder sb = new StringBuilder();
        sb.append("ENUM(");
        for (int i = 0; i < values.length; i++) {
            if (i != 0) sb.append(" ,");
            sb.append('\'').append(values[i]).append('\'');
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 生成 SET 数据类型对应的列类型字符串。
     *
     * <p>
     * SET 的格式为 <code>SET('value1','value2',...)</code>。
     *
     * <p>
     * 有关该格式的详细信息，请参考:
     * <a href="https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html">
     * https://dev.mysql.com/doc/refman/8.0/en/string-type-syntax.html
     * </a>
     *
     * @param values 参数 values。
     * @return SET 数据类型对应的列类型字符串。
     */
    @SuppressWarnings({"DuplicatedCode", "JavaExistingMethodCanBeUsed"})
    public static String set(String... values) {
        StringBuilder sb = new StringBuilder();
        sb.append("SET(");
        for (int i = 0; i < values.length; i++) {
            if (i != 0) sb.append(" ,");
            sb.append('\'').append(values[i]).append('\'');
        }
        sb.append(")");
        return sb.toString();
    }
}
