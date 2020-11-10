package com.dwarfeng.subgrade.sdk.database.ddl;

import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;
import org.springframework.lang.NonNull;

import java.sql.Connection;
import java.sql.Statement;

/**
 * MySQL8 建表数据库任务。
 *
 * <p>
 * TODO 该类未实现，在此仅作为占位，请勿使用。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
@SuppressWarnings("DuplicatedCode")
public class MySQL8CreateTableDatabaseTask implements DatabaseTask<Object> {

    private static final String CACHE_SQL_CREATE_TABLE = "CACHE_SQL_CREATE_TABLE";

    private TableDefinition tableDefinition;

    public MySQL8CreateTableDatabaseTask(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public Object todo(Connection connection) throws Exception {
        String sql = (String) tableDefinition.getOrPutCache(CACHE_SQL_CREATE_TABLE, provideCreateTableSQL());
        Statement statement = connection.createStatement();
        statement.execute(sql);
        return null;
    }

    private String provideCreateTableSQL() {
        /*
         * TODO 建表SQL模板
         *
         *  CREATE TABLE `fdr`.`new_table` (
         * `id` INT NOT NULL,
         * `content` VARCHAR(45) NULL,
         * `remark` VARCHAR(45) NULL,
         * `enabled` TINYINT NULL,
         * `fore` INT NULL,
         * PRIMARY KEY (`id`),
         * INDEX `foo` (`remark`(100) ASC, `enabled` ASC) KEY_BLOCK_SIZE = 2 WITH PARSER 2 INVISIBLE,
         * INDEX `bar` (`content` ASC) INVISIBLE,
         * CONSTRAINT `fff`
         *   FOREIGN KEY ()
         *   REFERENCES `fdr`.`tbl_filter_info` ()
         *   ON DELETE RESTRICT
         *   ON UPDATE CASCADE,
         * CONSTRAINT `ggg`
         *   FOREIGN KEY ()
         *   REFERENCES `fdr`.`tbl_trigger_info` ()
         *   ON DELETE SET NULL
         *   ON UPDATE RESTRICT);
         */
        return "";
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }
}
