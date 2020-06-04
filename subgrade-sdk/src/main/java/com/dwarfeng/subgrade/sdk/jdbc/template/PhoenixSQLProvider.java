package com.dwarfeng.subgrade.sdk.jdbc.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Phoenix SQL提供器。
 *
 * <p>
 * <a href="http://phoenix.apache.org/">Apache Phoenix</a>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class PhoenixSQLProvider extends ColumnDefinitionSQLProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoenixSQLProvider.class);

    public PhoenixSQLProvider(
            String tableName,
            List<ColumnDefinition> pkColumnDefinitions,
            List<ColumnDefinition> plainColumnDefinitions) {
        super(tableName, pkColumnDefinitions, plainColumnDefinitions);
    }

    @Override
    public String provideInsertSQL() {
        String sql = provideUpsertSQL();
        LOGGER.debug("provideInsertSQL: " + sql);
        return sql;
    }

    @Override
    public String provideUpdateSQL() {
        String sql = provideUpsertSQL();
        LOGGER.debug("provideUpdateSQL: " + sql);
        return sql;
    }

    private String provideUpsertSQL() {
        return String.format("UPSERT INTO %s (%s) VALUES (%s)", tableName, fullColumnSerial(), fullColumnPlaceHolder());
    }

    @Override
    public String provideDeleteSQL() {
        String sql = String.format("DELETE FROM %s WHERE %s = %s", tableName, pkColumnSerial(), pkColumnPlaceHolder());
        LOGGER.debug("provideDeleteSQL: " + sql);
        return sql;
    }

    @Override
    public String provideGetSQL() {
        String sql = String.format("SELECT %s FROM %s WHERE %s = %s",
                fullColumnSerial(), tableName, pkColumnSerial(), pkColumnPlaceHolder());
        LOGGER.debug("provideGetSQL: " + sql);
        return sql;
    }

    @Override
    public String provideExistsSQL() {
        String sql = String.format("SELECT 1 FROM %s WHERE %s = %s", tableName, pkColumnSerial(), pkColumnPlaceHolder());
        LOGGER.debug("provideExistsSQL: " + sql);
        return sql;
    }

    /**
     * 获取创建表序列对应的SQL语句。
     *
     * <p>
     * 生成如下SQL语句的下划线部分。<br>
     * <code>CREATE TABLE IF NOT EXISTS t (
     * <u>id BIGINT NOT NULL PRIMARY KEY, name VARCHAR(10), age INT DEFAULT 18</u>) SALT_BUCKETS=30</code>
     *
     * @return 创建表序列对应的SQL语句。
     */
    protected String createTableSerial() {
        StringBuilder sb = new StringBuilder();
        //遍历主键列定义，添加定义。
        for (int i = 0; i < pkColumnDefinitions.size(); i++) {
            ColumnDefinition columnDefinition = pkColumnDefinitions.get(i);
            if (i == 0) {
                //添加主键字段名称。
                sb.append(columnDefinition.getName());
            } else {
                //添加主键字段名称。
                sb.append(", ").append(columnDefinition.getName());
            }
            //添加主键字段类型。
            sb.append(" ").append(columnDefinition.getType());
            //如果字段不能为 null 则添加 NOT NULL。
            if (!columnDefinition.isNullable()) {
                sb.append(" NOT NULL");
            }
            //如果默认值不为null，则添加 DEFAULT
            if (Objects.nonNull(columnDefinition.getDefaultValue())) {
                sb.append(" DEFAULT ").append(columnDefinition.getDefaultValue());
            }
        }
        //遍历一般列定义，添加定义。
        for (ColumnDefinition plainColumnDefinition : plainColumnDefinitions) {
            //添加字段名称。
            sb.append(", ").append(plainColumnDefinition.getName());
            //添加字段类型。
            sb.append(' ').append(plainColumnDefinition.getType());
            //如果字段不能为 null 则添加 NOT NULL。
            if (!plainColumnDefinition.isNullable()) {
                sb.append(" NOT NULL");
            }
            //如果默认值不为null，则添加 DEFAULT
            if (Objects.nonNull(plainColumnDefinition.getDefaultValue())) {
                sb.append(" DEFAULT ").append(plainColumnDefinition.getDefaultValue());
            }
        }
        return sb.toString();
    }
}
