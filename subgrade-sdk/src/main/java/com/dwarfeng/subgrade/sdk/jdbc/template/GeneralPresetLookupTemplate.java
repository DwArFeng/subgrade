package com.dwarfeng.subgrade.sdk.jdbc.template;

import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用预设查询模板。
 *
 * <p>
 * 预设查询模板的通用抽象实现。
 * <p>
 * 该类是为了继承而设计的，继承该类，并重写该类的方法，可实现不同数据库的预设查询模板。<br>
 * 该类提供了 SQL 片段的缓存工具，您可以使用某一个键存储一个 SQL 片段，并重新从缓存中取出，这样做可以增加 SQL 的生成效率。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public abstract class GeneralPresetLookupTemplate implements PresetLookupTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralPresetLookupTemplate.class);

    protected TableDefinition tableDefinition;

    private final SQLFragmentCache sqlFragmentCache = new SQLFragmentCache();

    public GeneralPresetLookupTemplate(@NotNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public abstract String lookupSQL(String preset, Object[] args);

    @Override
    public abstract String pagingSQL(String preset, Object[] args, PagingInfo pagingInfo);

    @Override
    public abstract String countSQL(String preset, Object[] args);

    protected final boolean containsCachedSQLFragment(String key) {
        return sqlFragmentCache.containsKey(key);
    }

    protected final String getCachedSQLFragment(String key) {
        String sqlFragment = sqlFragmentCache.get(key);
        LOGGER.debug("using cache: key = " + key + ", sqlFragment = " + sqlFragment);
        return sqlFragment;
    }

    protected final void putCachedSQLFragment(String key, String sqlFragment) {
        LOGGER.debug("putting cache: key = " + key + ", sqlFragment = " + sqlFragment);
        sqlFragmentCache.put(key, sqlFragment);
    }

    protected final void removeCachedSQLFragment(String key) {
        LOGGER.debug("removing cache: key = " + key);
        sqlFragmentCache.remove(key);
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(@NonNull TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public String toString() {
        return "GeneralPresetLookupTemplate{" +
                "tableDefinition=" + tableDefinition +
                '}';
    }

    private static class SQLFragmentCache {

        private final Map<String, String> cacheMap = new HashMap<>();

        public boolean containsKey(String key) {
            return cacheMap.containsKey(key);
        }

        public String get(String key) {
            return cacheMap.get(key);
        }

        public String put(String key, String sqlFragment) {
            return cacheMap.put(key, sqlFragment);
        }

        public void remove(String key) {
            cacheMap.remove(key);
        }
    }
}
