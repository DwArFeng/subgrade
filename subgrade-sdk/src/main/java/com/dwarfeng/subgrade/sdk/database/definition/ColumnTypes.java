package com.dwarfeng.subgrade.sdk.database.definition;

import javax.annotation.Nonnull;

/**
 * 列类型工具类。
 * <p>
 * 下列代码字母用于描述中：<br>
 * M  指出最大的显示尺寸。最大的合法的显示尺寸是 255 。<br>
 * D  适用于浮点类型并且指出跟随在十进制小数点后的数码的数量。最大可能的值是30，但是应该不大于M-2。<br>
 * 方括号(“[”和“]”)指出可选的类型修饰符的部分。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public final class ColumnTypes {

    /**
     * TINYINT[(M)] 一个很小的整数。有符号的范围是-128到127，无符号的范围是0到255。
     */
    public static String tinyint() {
        return "TINYINT";
    }

    /**
     * TINYINT[(M)] 一个很小的整数。有符号的范围是-128到127，无符号的范围是0到255。
     */
    public static String tinyint(int m) {
        return String.format("TINYINT(%d)", m);
    }

    /**
     * SMALLINT[(M)] 一个小整数。有符号的范围是-32768到32767，无符号的范围是0到65535。
     */
    public static String smallint() {
        return "SMALLINT";
    }

    /**
     * SMALLINT[(M)] 一个小整数。有符号的范围是-32768到32767，无符号的范围是0到65535。
     */
    public static String smallint(int m) {
        return String.format("SMALLINT(%d)", m);
    }

    /**
     * MEDIUMINT[(M)] 一个中等大小整数。有符号的范围是-8388608到8388607，无符号的范围是0到16777215。
     */
    public static String mediumint() {
        return "MEDIUMINT";
    }

    /**
     * MEDIUMINT[(M)] 一个中等大小整数。有符号的范围是-8388608到8388607，无符号的范围是0到16777215。
     */
    public static String mediumint(int m) {
        return String.format("MEDIUMINT(%d)", m);
    }

    /**
     * INT[(M)] 一个正常大小整数。有符号的范围是-2147483648到2147483647，无符号的范围是0到4294967295。
     */
    public static String sqlInt() {
        return "INT";
    }

    /**
     * INT[(M)] 一个正常大小整数。有符号的范围是-2147483648到2147483647，无符号的范围是0到4294967295。
     */
    public static String sqlInt(int m) {
        return String.format("INT(%d)", m);
    }

    /**
     * INTEGER[(M)] 这是INT的一个同义词。
     */
    public static String sqlInteger() {
        return "INTEGER";
    }

    /**
     * INTEGER[(M)] 这是INT的一个同义词。
     */
    public static String sqlInteger(int m) {
        return String.format("INTEGER(%d)", m);
    }

    /**
     * BIGINT[(M)] 一个大整数。有符号的范围是-9223372036854775808到9223372036854775807，
     * 无符号的范围是0到18446744073709551615。
     */
    public static String bigint() {
        return "BIGINT";
    }

    /**
     * BIGINT[(M)] 一个大整数。有符号的范围是-9223372036854775808到9223372036854775807，
     * 无符号的范围是0到18446744073709551615。
     */
    public static String bigint(int m) {
        return String.format("BIGINT(%d)", m);
    }

    /**
     * FLOAT[(M,D)] 一个小(单精密)浮点数字。不能无符号。允许的值是-3.402823466E+38到-1.175494351E-38，0
     * 和1.175494351E-38到3.402823466E+38。M是显示宽度而D是小数的位数。没有参数的FLOAT或有小于24的参数表示一个单精密浮点数字。
     */
    public static String sqlFloat() {
        return "FLOAT";
    }

    /**
     * FLOAT[(M,D)] 一个小(单精密)浮点数字。不能无符号。允许的值是-3.402823466E+38到-1.175494351E-38，0
     * 和1.175494351E-38到3.402823466E+38。M是显示宽度而D是小数的位数。没有参数的FLOAT或有小于24的参数表示一个单精密浮点数字。
     */
    public static String sqlFloat(int m, int d) {
        return String.format("FLOAT(%d,%d)", m, d);
    }

    /**
     * DOUBLE[(M,D)] 一个正常大小(双精密)浮点数字。不能无符号。允许的值是-1.7976931348623157E+308到-2.2250738585072014E-308,
     * 0和2.2250738585072014E-308到1.7976931348623157E+308。M是显示宽度而D是小数位数。
     * 没有一个参数的DOUBLE或FLOAT(X)（25 < = X < = 53）代表一个双精密浮点数字。
     */
    public static String sqlDouble() {
        return "DOUBLE";
    }

    /**
     * DOUBLE[(M,D)] 一个正常大小(双精密)浮点数字。不能无符号。允许的值是-1.7976931348623157E+308到-2.2250738585072014E-308,
     * 0和2.2250738585072014E-308到1.7976931348623157E+308。M是显示宽度而D是小数位数。
     * 没有一个参数的DOUBLE或FLOAT(X)（25 < = X < = 53）代表一个双精密浮点数字。
     */
    public static String sqlDouble(int m, int d) {
        return String.format("DOUBLE(%d,%d)", m, d);
    }

    /**
     * DECIMAL[(M[,D])] 一个未压缩(unpack)的浮点数字。不能无符号。
     * 行为如同一个CHAR列：“未压缩”意味着数字作为一个字符串被存储，值的每一位使用一个字符。
     * 小数点，并且对于负数，“-”符号不在M中计算。如果D是0，值将没有小数点或小数部分。
     * DECIMAL值的最大范围与DOUBLE相同，但是对一个给定的DECIMAL列，实际的范围可以通过M和D的选择被限制。
     * 如果D被省略，它被设置为0。如果M被省掉，它被设置为10。注意，在MySQL3.22里，M参数包括符号和小数点。
     */
    public static String decimal() {
        return "DECIMAL";
    }

    /**
     * DECIMAL[(M[,D])] [ZEROFILL] 一个未压缩(unpack)的浮点数字。不能无符号。
     * 行为如同一个CHAR列：“未压缩”意味着数字作为一个字符串被存储，值的每一位使用一个字符。
     * 小数点，并且对于负数，“-”符号不在M中计算。如果D是0，值将没有小数点或小数部分。
     * DECIMAL值的最大范围与DOUBLE相同，但是对一个给定的DECIMAL列，实际的范围可以通过M和D的选择被限制。
     * 如果D被省略，它被设置为0。如果M被省掉，它被设置为10。注意，在MySQL3.22里，M参数包括符号和小数点。
     */
    public static String decimal(int m) {
        return String.format("DECIMAL(%d)", m);
    }

    /**
     * DECIMAL[(M[,D])] [ZEROFILL] 一个未压缩(unpack)的浮点数字。不能无符号。
     * 行为如同一个CHAR列：“未压缩”意味着数字作为一个字符串被存储，值的每一位使用一个字符。
     * 小数点，并且对于负数，“-”符号不在M中计算。如果D是0，值将没有小数点或小数部分。
     * DECIMAL值的最大范围与DOUBLE相同，但是对一个给定的DECIMAL列，实际的范围可以通过M和D的选择被限制。
     * 如果D被省略，它被设置为0。如果M被省掉，它被设置为10。注意，在MySQL3.22里，M参数包括符号和小数点。
     */
    public static String decimal(int m, int d) {
        return String.format("DECIMAL(%d,%d)", m, d);
    }

    /**
     * NUMERIC[(M[,D])] 这是DECIMAL的一个同义词。
     */
    public static String numeric() {
        return "NUMERIC";
    }

    /**
     * NUMERIC[(M[,D])] 这是DECIMAL的一个同义词。
     */
    public static String sqlNumeric(int m) {
        return String.format("NUMERIC(%d)", m);
    }

    /**
     * NUMERIC[(M[,D])] 这是DECIMAL的一个同义词。
     */
    public static String sqlNumeric(int m, int d) {
        return String.format("NUMERIC(%d,%d)", m, d);
    }

    /**
     * DATE 一个日期。支持的范围是'1000-01-01'到'9999-12-31'。
     * Database 以'YYYY-MM-DD'格式来显示DATE值，但是允许你使用字符串或数字把值赋给DATE列。
     */
    public static String date() {
        return "DATE";
    }

    /**
     * DATETIME 一个日期和时间组合。支持的范围是'1000-01-01 00:00:00'到'9999-12-31 23:59:59'。
     * Database 以'YYYY-MM-DD HH:MM:SS'格式来显示 DATETIME值，但是允许你使用字符串或数字把值赋给DATETIME的列。
     */
    public static String datetime() {
        return "DATETIME";
    }

    /**
     * TIMESTAMP[(M)]  一个时间戳记。范围是'1970-01-01 00:00:00'到2037年的某时。
     */
    public static String timestamp() {
        return "TIMESTAMP";
    }

    /**
     * TIMESTAMP[(M)]  一个时间戳记。范围是'1970-01-01 00:00:00'到2037年的某时。
     */
    public static String timestamp(int m) {
        return String.format("TIMESTAMP(%d)", m);
    }

    /**
     * YEAR[(2|4)] 一个2或4位数字格式的年(缺省是4位)。
     * 允许的值是1901到2155，和0000（4位年格式），如果你使用2位，1970-2069( 70-69)。
     * MySQL以YYYY格式来显示YEAR值，但是允许你把使用字符串或数字值赋给YEAR列。
     */
    public static String year() {
        return "YEAR";
    }

    /**
     * YEAR 类型的位数。
     *
     * @author DwArFeng
     * @since 1.1.0
     */
    public enum YearDigit {
        TWO, FOUR
    }

    /**
     * YEAR[(2|4)] 一个2或4位数字格式的年(缺省是4位)。
     * 允许的值是1901到2155，和0000（4位年格式），如果你使用2位，1970-2069( 70-69)。
     * MySQL以YYYY格式来显示YEAR值，但是允许你把使用字符串或数字值赋给YEAR列。
     */
    public static String year(@Nonnull YearDigit yearDigit) {
        switch (yearDigit) {
            case TWO:
                return "YEAR(2)";
            case FOUR:
                return "YEAR(4)";
            default:
                return "YEAR";
        }
    }

    /**
     * CHAR[(M)] 一个定长字符串，当存储时，总是是用空格填满右边到指定的长度。
     * M的范围是1 ～ 255个字符。当值被检索时，空格尾部被删除。
     */
    public static String sqlChar() {
        return "CHAR";
    }

    /**
     * CHAR[(M)] 一个定长字符串，当存储时，总是是用空格填满右边到指定的长度。
     * M的范围是1 ～ 255个字符。当值被检索时，空格尾部被删除。
     */
    public static String sqlChar(int m) {
        return String.format("CHAR(%d)", m);
    }

    /**
     * 一个变长字符串。注意：当值被存储时，尾部的空格被删除(这不同于ANSI SQL规范)。M的范围是1 ～ 255个字符。
     */
    public static String varchar() {
        return "VARCHAR";
    }

    /**
     * 一个变长字符串。注意：当值被存储时，尾部的空格被删除(这不同于ANSI SQL规范)。M的范围是1 ～ 255个字符。
     */
    public static String varchar(int m) {
        return String.format("VARCHAR(%d)", m);
    }

    /**
     * TINYBLOB 一个BLOB或TEXT列，最大长度为255(2^8-1)个字符或等大小的二进制数据。
     */
    public static String tinyblob() {
        return "TINYBLOB";
    }

    /**
     * TINYTEXT 一个BLOB或TEXT列，最大长度为255(2^8-1)个字符或等大小的二进制数据。
     */
    public static String tinytext() {
        return "TINYTEXT";
    }

    /**
     * BLOB 一个BLOB或TEXT列，最大长度为65535(2^16-1)个字符或等大小的二进制数据。
     */
    public static String blob() {
        return "BLOB";
    }

    /**
     * TEXT 一个BLOB或TEXT列，最大长度为65535(2^16-1)个字符或等大小的二进制数据。
     */
    public static String text() {
        return "TEXT";
    }

    /**
     * MEDIUMBLOB 一个BLOB或TEXT列，最大长度为16777215(2^24-1)个字符或等大小的二进制数据。
     */
    public static String mediumblob() {
        return "MEDIUMBLOB";
    }

    /**
     * MEDIUMTEXT 一个BLOB或TEXT列，最大长度为16777215(2^24-1)个字符或等大小的二进制数据。
     */
    public static String mediumtext() {
        return "MEDIUMTEXT";
    }

    /**
     * LONGBLOB 一个BLOB或TEXT列，最大长度为4294967295(2^32-1)个字符或等大小的二进制数据。
     */
    public static String longblob() {
        return "LONGBLOB";
    }

    /**
     * LONGTEXT 一个BLOB或TEXT列，最大长度为4294967295(2^32-1)个字符或等大小的二进制数据。
     */
    public static String longtext() {
        return "LONGTEXT";
    }

    /**
     * ENUM('value1','value2',...)  枚举。一个仅有一个值的字符串对象，这个值式选自与值列表 'value1'、'value2', ...,或NULL。
     * 一个ENUM最多能有65535不同的值。
     */
    @SuppressWarnings("DuplicatedCode")
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
     * SET('value1','value2',...) 一个集合。
     * 能有零个或多个值的一个字符串对象，其中每一个必须从值列表 'value1','value2',...选出。
     * 一个SET最多能有64个成员。
     */
    @SuppressWarnings("DuplicatedCode")
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
