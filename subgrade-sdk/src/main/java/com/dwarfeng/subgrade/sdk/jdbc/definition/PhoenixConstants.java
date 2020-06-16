package com.dwarfeng.subgrade.sdk.jdbc.definition;

/**
 * Phoenix 数据库表定义常量。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public final class PhoenixConstants {

    public static final String CUSTOM_NULLABLE = "NULLABLE";
    public static final String CUSTOM_DEFAULT = "DEFAULT";
    public static final String NAME_PRIMARY_KEY = "PK";
    public static final String TYPE_PRIMARY_KEY = "PRIMARY_KEY";
    public static final String CUSTOM_ASC = "ASC";
    public static final String CUSTOM_DESC = "DESC";
    public static final String CUSTOM_ROW_TIMESTAMP = "ROW_TIMESTAMP";
    public static final String CUSTOM_SALT_BUCKETS = "SALT_BUCKETS";
    public static final String CUSTOM_DISABLE_WAL = "DISABLE_WAL";
    public static final String CUSTOM_IMMUTABLE_ROWS = "IMMUTABLE_ROWS";
    public static final String CUSTOM_MULTI_TENANT = "MULTI_TENANT";
    public static final String CUSTOM_DEFAULT_COLUMN_FAMILY = "DEFAULT_COLUMN_FAMILY";
    public static final String CUSTOM_STORE_NULLS = "STORE_NULLS";
    public static final String CUSTOM_TRANSACTIONAL = "TRANSACTIONAL";
    public static final String CUSTOM_UPDATE_CACHE_FREQUENCY = "UPDATE_CACHE_FREQUENCY";
    public static final String CUSTOM_APPEND_ONLY_SCHEMA = "APPEND_ONLY_SCHEMA";
    public static final String CUSTOM_AUTO_PARTITION_SEQ = "AUTO_PARTITION_SEQ";
    public static final String CUSTOM_GUIDE_POSTS_WIDTH = "GUIDE_POSTS_WIDTH";
    public static final String CUSTOM_SPLIT_POINT = "SPLIT_POINT";
    public static final String INDEX_TYPE_GLOBAL = "GLOBAL";
    public static final String INDEX_TYPE_LOCAL = "LOCAL";
    public static final String CUSTOM_INCLUDE = "INCLUDE";
    public static final String CUSTOM_ASYNC = "ASYNC";

    public enum ColumnNullable {
        NULL, NOT_NULL
    }

    public enum UpdateCacheFrequency {
        ALWAYS, NEVER
    }

    public enum IndexType {
        GLOBAL, LOCAL
    }

    public enum IndexAsyncType {
        ASYNC, NOT_ASYNC
    }

    private PhoenixConstants() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
