package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class ColumnDefinitionSQLProvider implements SQLProvider {

    protected final String tableName;
    protected final List<ColumnDefinition> pkColumnDefinitions;
    protected final List<ColumnDefinition> plainColumnDefinitions;

    public ColumnDefinitionSQLProvider(
            @NonNull String tableName,
            @NonNull List<ColumnDefinition> pkColumnDefinitions,
            @NonNull List<ColumnDefinition> plainColumnDefinitions) {
        validate(pkColumnDefinitions, plainColumnDefinitions);
        this.tableName = tableName;
        this.pkColumnDefinitions = pkColumnDefinitions;
        this.plainColumnDefinitions = plainColumnDefinitions;
    }

    private void validate(
            @NonNull List<ColumnDefinition> pkColumnDefinitions,
            @NonNull List<ColumnDefinition> plainColumnDefinitions) {
        if (pkColumnDefinitions.isEmpty()) {
            throw new IllegalArgumentException("参数 pkColumnDefinitions 不能为空");
        }
        if (CollectionUtil.conatinsNull(pkColumnDefinitions)) {
            throw new IllegalArgumentException("参数 pkColumnDefinitions 不能含有 null 元素");
        }
        if (CollectionUtil.conatinsNull(plainColumnDefinitions)) {
            throw new IllegalArgumentException("参数 plainColumnDefinitions 不能含有 null 元素");
        }
    }

    @Override
    public abstract String provideInsertSQL();

    @Override
    public abstract String provideUpdateSQL();

    @Override
    public abstract String provideDeleteSQL();

    @Override
    public abstract String provideGetSQL();

    @Override
    public abstract String provideExistsSQL();

    @Override
    public List<String> provideCreateTableSQL() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnDefinition> getPkColumnDefinitions() {
        return pkColumnDefinitions;
    }

    public List<ColumnDefinition> getPlainColumnDefinitions() {
        return plainColumnDefinitions;
    }

    /**
     * 获取全序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT <u>id, name, age</u> FROM t</code><br>
     * <code>INSERT INTO t (<u>id, name, age</u>) VALUES (?, ?, ?)</code>
     *
     * @return 全序列对应的SQL语句。
     */
    protected String fullColumnSerial() {
        StringBuilder sb = new StringBuilder();
        appendPkColumnSerial(sb);
        for (ColumnDefinition plainColumnDefinition : plainColumnDefinitions) {
            sb.append(", ").append(plainColumnDefinition.getName());
        }
        return sb.toString();
    }

    /**
     * 获取全占位符对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>INSERT INTO t (id, name, age) VALUES (<u>?, ?, ?</u>)</code>
     *
     * @return 全占位符对应的SQL语句。
     */
    protected String fullColumnPlaceHolder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pkColumnDefinitions.size(); i++) {
            if (i == 0) {
                sb.append('?');
            } else {
                sb.append(", ?");
            }
        }
        for (int i = 0; i < plainColumnDefinitions.size(); i++) {
            sb.append(", ?");
        }
        return sb.toString();
    }

    /**
     * 获取主键序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT id, name, age FROM t WHERE <u>id</u> = ?</code><br>
     * <code>DELETE FROM t WHERE <u>id</u> = ?</code>
     *
     * @return 主键序列对应的SQL语句。
     */
    protected String pkColumnSerial() {
        StringBuilder sb = new StringBuilder();
        appendPkColumnSerial(sb);
        return sb.toString();
    }

    private void appendPkColumnSerial(StringBuilder sb) {
        for (int i = 0; i < pkColumnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = pkColumnDefinitions.get(i);
            if (i == 0) {
                sb.append(columnDefinition.getName());
            } else {
                sb.append(", ").append(columnDefinition.getName());
            }
        }
    }

    /**
     * 获取主键占位符对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>SELECT id, name, age FROM t WHERE id = <u>?</u></code><br>
     * <code>DELETE FROM t WHERE id = <u>?</u></code>
     *
     * @return 主键占位符对应的SQL语句。
     */
    protected String pkColumnPlaceHolder() {
        return "?";
    }

    /**
     * 列定义。
     *
     * @author DwArFeng
     * @since 1.1.0
     */
    public static final class ColumnDefinition {

        private final String name;
        private final String type;
        private final boolean unique;
        private final boolean nullable;
        private final String defaultValue;

        public ColumnDefinition(String name, String type, boolean unique, boolean nullable, String defaultValue) {
            this.name = name;
            this.type = type;
            this.unique = unique;
            this.nullable = nullable;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isUnique() {
            return unique;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isNullable() {
            return nullable;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return "ColumnDefinition{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", unique=" + unique +
                    ", nullable=" + nullable +
                    ", defaultValue='" + defaultValue + '\'' +
                    '}';
        }
    }
}
