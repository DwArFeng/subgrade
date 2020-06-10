package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 通用建表模板。
 *
 * <p>
 * 建表模板的通用抽象实现。
 * <p>
 * 该类是为了继承而设计的，继承该类，并重写该类的内部方法，可以实现不同数据库的 Crud 模板。<br>
 * 重写该类时，请重写该类的内部方法， {@link #internalCreateTableSQL()}，
 * 而不是重写直接继承的方法 {@link #createTableSQL()}。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class GeneralCreateTableTemplate implements CreateTableTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralCreateTableTemplate.class);

    protected TableDefinition tableDefinition;

    public GeneralCreateTableTemplate(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public List<String> createTableSQL() {
        List<String> sqlList = internalCreateTableSQL();
        LOGGER.debug("createTableSQL: ");
        for (int i = 0; i < sqlList.size(); i++) {
            LOGGER.debug("\t" + i + ". " + sqlList.get(i));
        }
        return sqlList;
    }

    /**
     * 获取内部的建表SQL语句。
     *
     * @return 内部的建表SQL语句。
     */
    protected abstract List<String> internalCreateTableSQL();

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public String toString() {
        return "GeneralCreateTableTemplate{" +
                "tableDefinition=" + tableDefinition +
                '}';
    }
}
